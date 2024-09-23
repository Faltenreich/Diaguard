package com.faltenreich.diaguard.preference.license

import com.mikepenz.aboutlibraries.entity.Library

sealed interface LicenseListIntent {

    data class OpenWebsite(val library: Library) : LicenseListIntent
}