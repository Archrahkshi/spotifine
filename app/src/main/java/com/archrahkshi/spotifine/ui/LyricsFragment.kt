package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.LyricsAdapter
import com.archrahkshi.spotifine.data.TRANSLATOR_API_KEY
import com.archrahkshi.spotifine.data.TRANSLATOR_URL
import com.archrahkshi.spotifine.data.TRANSLATOR_VERSION
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
            val originalLyrics = getOriginalLyrics()
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
                    LyricsFragment(!isLyricsTranslated)
                )?.commit()
            }
        }
    }

    private suspend fun getOriginalLyrics() = withContext(Dispatchers.IO) {
        """
            Flying over darkened skies the battle will call
            Distant angels crying in the eye of the storm
            And the world falls under the starlight shining from heavens below
            Long years of pain and sorrow searching for more
            Cry for the touch of angels never before
            And the stars fall on the horizon onwards and up through the pain
            
            Ride the wind and fight the demon steel shining bright
            Standing together forever onwards flames burning strong
            Hot wind in hell of pain and sorrow now and ever onwards
            We stare into the dawn of a new world
            
            [Pre-Chorus]
            Cry out for the fallen heroes
            Lost in time ago
            In our minds they still belong
            When the sands of time are gone
            
            [Chorus]
            Rise over shadow mountains blazing with power
            Crossing valleys endless tears in unity we stand
            Far and wide across the land the victory is ours
            On towards the gates of reason
            Fight for the truth and the freedom
            Gloria
            
            Searching through the memories to open the door
            Living on the edge of life like never before
            And the ground chants under the moonlight facing their fears all the same
            
            Heavens fear now open wide and up for the the call
            All in stark reality the angels will fall
            And the world cries out for the silence lost in the voices unknown
            
            Blinded by the force of evil cries into the night
            Never before have they seen the darkness now they are all gone
            Out from the shadows storming on the wings of revelations
            Your soul will feel no mercy come the dawn
            
            Hold on for the morning after
            Never to let go
            In the fire's burning strong
            When the tides of time roll on
            
            [Chorus]
            
            [Pre-Chorus]
            
            [Chorus]
        """.trimIndent()
    }

    private suspend fun getTranslatedLyrics(
        lyricsOriginal: String
    ) = withContext(Dispatchers.IO) {
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
