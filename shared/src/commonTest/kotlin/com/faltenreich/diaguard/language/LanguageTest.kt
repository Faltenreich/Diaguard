package com.faltenreich.diaguard.language

import kotlin.test.Test
import kotlin.test.assertEquals

class LanguageTest {

    @Test
    fun `parses language code for German`() {
        assertEquals(Language.GERMAN, Language.fromLanguageCode("de"))
    }

    @Test
    fun `parses language code for English`() {
        assertEquals(Language.ENGLISH, Language.fromLanguageCode("en"))
    }

    @Test
    fun `parses language code for Spanish`() {
        assertEquals(Language.SPANISH, Language.fromLanguageCode("es"))
    }

    @Test
    fun `parses language code for French`() {
        assertEquals(Language.FRENCH, Language.fromLanguageCode("fr"))
    }

    @Test
    fun `parses language code for Italian`() {
        assertEquals(Language.ITALIAN, Language.fromLanguageCode("it"))
    }

    @Test
    fun `parses language code for Dutch`() {
        assertEquals(Language.DUTCH, Language.fromLanguageCode("nl"))
    }
}