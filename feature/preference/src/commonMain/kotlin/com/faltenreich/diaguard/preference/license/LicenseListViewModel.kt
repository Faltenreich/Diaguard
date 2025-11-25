package com.faltenreich.diaguard.preference.license

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.system.web.UrlOpener
import kotlinx.coroutines.flow.emptyFlow

internal class LicenseListViewModel(
    private val urlOpener: UrlOpener,
) : ViewModel<Unit, LicenseListIntent, Unit>() {

    override val state = emptyFlow<Unit>()

    override suspend fun handleIntent(intent: LicenseListIntent) {
        when (intent) {
            is LicenseListIntent.OpenWebsite -> intent.library.website?.let(urlOpener::open)
        }
    }
}