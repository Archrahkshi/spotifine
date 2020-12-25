package com.archrahkshi.spotifine.util

import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException
import com.ibm.watson.language_translator.v3.LanguageTranslator
import com.ibm.watson.language_translator.v3.model.IdentifyOptions
import com.ibm.watson.language_translator.v3.model.TranslateOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val languageTranslator = LanguageTranslator(
        TRANSLATOR_VERSION,
        IamAuthenticator(TRANSLATOR_API_KEY)
).apply { serviceUrl = TRANSLATOR_URL }

suspend fun String.identifyLanguage(): String = withContext(Dispatchers.IO) {
    languageTranslator
            .identify(IdentifyOptions.Builder().text(this@identifyLanguage).build())
            .execute()
            .result
            .languages.first().language
}

suspend fun String.translateFromTo(source: String, target: String) = withContext(Dispatchers.IO) {
    try {
        languageTranslator.translate(
                TranslateOptions.Builder()
                        // Line separators must be doubled for the lyrics to be translated line by line,
                        // not as a uniform text
                        .addText(this@translateFromTo.replace("\n", "\n\n"))
                        .source(source)
                        .target(target)
                        .build()
        ).execute().result.translations.first().translation
                .replace("\n\n", "\n") // Returning to the original line separators
    } catch (e: ServiceResponseException) {
        null
    }
}
