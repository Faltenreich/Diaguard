package com.faltenreich.rhyme.language

enum class Language(
    val code: String,
    val title: String,
) {

    GERMAN("de", "Deutsch"),
    ENGLISH("en", "English"),
    SPANISH("es", "Español"),
    FRENCH("fr", "Français"),
    ITALIAN("it", "Italiano"),
    DUTCH("nl", "Nederlands"),
    ;

    companion object {

        val default = ENGLISH

        fun fromLanguageCode(languageCode: String): Language? {
            return values().firstOrNull { language -> language.code == languageCode }
        }
    }
}