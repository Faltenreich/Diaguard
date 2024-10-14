package com.faltenreich.diaguard.preference.license

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.networking.UrlOpener
import kotlinx.coroutines.flow.map

class LicenseListViewModel(
    getLicenses: GetLicensesUseCase,
    private val urlOpener: UrlOpener,
) : ViewModel<LicenseListState, LicenseListIntent, Unit>() {

    override val state = getLicenses().map(::LicenseListState)

    override suspend fun handleIntent(intent: LicenseListIntent) {
        when (intent) {
            is LicenseListIntent.OpenWebsite -> intent.library.website?.let(urlOpener::open)
        }
    }
}