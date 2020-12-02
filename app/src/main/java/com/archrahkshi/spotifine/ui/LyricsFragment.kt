package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.LyricsAdapter
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.GENIUS_ACCESS_TOKEN
import com.archrahkshi.spotifine.util.GENIUS_API_BASE_URL
import com.archrahkshi.spotifine.util.GENIUS_BASE_URL
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.TRANSLATOR_API_KEY
import com.archrahkshi.spotifine.util.TRANSLATOR_URL
import com.archrahkshi.spotifine.util.TRANSLATOR_VERSION
import com.google.gson.JsonParser
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.model.TranslateOptions
import com.ibm.watson.language_translator.v3.util.Language.RUSSIAN
import kotlinx.android.synthetic.main.fragment_lyrics.buttonTranslate
import kotlinx.android.synthetic.main.fragment_lyrics.recyclerViewLyrics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.util.Locale
import kotlin.coroutines.CoroutineContext

class LyricsFragment(
    private val isLyricsTranslated: Boolean,
    override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : Fragment(), CoroutineScope {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lyrics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            val name = arguments?.getString(NAME) ?: ""
            val artists = arguments?.getString(ARTISTS) ?: ""
            val originalLyrics = getOriginalLyrics(name, artists)
            if (originalLyrics == null) {
                recyclerViewLyrics.adapter = LyricsAdapter(
                    listOf("It seems like this song is instrumental... or is it not?")
                )
                buttonTranslate.visibility = View.GONE
            } else {
                if (isLyricsTranslated) {
                    val (detectedLanguage, translatedLyrics) = getTranslatedLyrics(originalLyrics)
                    val appLocale = Locale(RUSSIAN)
                    buttonTranslate.text = getString(
                        R.string.detected_language,
                        Locale(detectedLanguage).getDisplayLanguage(appLocale),
                        appLocale.getDisplayLanguage(appLocale)
                    )
                    recyclerViewLyrics.adapter = LyricsAdapter(translatedLyrics.split('\n'))
                } else {
                    buttonTranslate.text = getString(R.string.translate)
                    recyclerViewLyrics.adapter = LyricsAdapter(originalLyrics.split('\n'))
                }

                buttonTranslate.setOnClickListener {
                    fragmentManager?.beginTransaction()?.replace(
                        R.id.frameLayoutPlayer,
                        LyricsFragment(!isLyricsTranslated).apply {
                            arguments = Bundle().apply {
                                putString(NAME, name)
                                putString(ARTISTS, artists)
                            }
                        }
                    )?.commit()
                }
            }
        }
    }

    private suspend fun getOriginalLyrics(
        title: String,
        artists: String
    ) = withContext(Dispatchers.IO) {
        /*val title = "Rise"
        val artists = "Disturbed"*/
        val songInfo = JsonParser().parse(
            buildGeniusRequest(
                "$GENIUS_API_BASE_URL/search?q=$artists $title".replace(" ", "%20")
            )
        ).asJsonObject["response"].asJsonObject["hits"].asJsonArray.find {
            it.asJsonObject["type"].asString == "song"
        }
        var lyricsFromPath = ""
        if (songInfo != null){
            lyricsFromPath = getLyricsFromPath(songInfo.asJsonObject["result"].asJsonObject["path"].asString) ?: "Something went wrong, sorry<not sorry> :("
            lyricsFromPath.deleteTrash()
        }
        else {
            Log.wtf("Genius", "no song info")
            null
        }
    }

    private fun getLyricsFromPath(path: String) = try {
        Jsoup.parse(buildGeniusRequest("$GENIUS_BASE_URL$path")).run {
            select("div.lyrics")
                .first()
                ?.select("p")
                ?.first()
                ?.html()
                ?.replace("<br> ", "\n")
        }
    } catch (e: Exception) {
        Log.wtf("Jsoup", e)
        null
    }

    private fun String.deleteTrash(): String {
        var str = this
        while (str.indexOf("<") != -1){
            str = str.replace(str.substring(str.indexOf("<"), str.indexOf(">") + 1), "")
        }
        return str
    }

    private fun buildGeniusRequest(url: String) = OkHttpClient().newCall(
        Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $GENIUS_ACCESS_TOKEN")
            //.header("User-Agent", "Chrome/51.0.2704.103 Mobile")
            .build()
    ).execute().body?.string()

    private suspend fun getTranslatedLyrics(lyricsOriginal: String): Pair<String, String> =
        withContext(Dispatchers.IO) {
            val result = LanguageTranslator(
                TRANSLATOR_VERSION,
                IamAuthenticator(TRANSLATOR_API_KEY)
            ).apply {
                serviceUrl = TRANSLATOR_URL
            }.translate(
                TranslateOptions.Builder()
                    // Line separators must be doubled for the lyrics to be translated line by line,
                    // not as a uniform text
                    .addText(lyricsOriginal.replace("\n", "\n\n"))
                    .target(RUSSIAN)
                    .build()
            ).execute().result
            Pair(
                result.detectedLanguage,
                // Returning to the original line separators
                result.translations.first().translation.replace("\n\n", "\n")
            )
        }
}
