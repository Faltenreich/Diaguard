package com.faltenreich.diaguard.preference.list.item.about

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.networking.UrlOpener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetSourceCodePreferenceUseCase(
    private val urlOpener: UrlOpener,
    private val localization: Localization,
) {

    operator fun invoke(): Flow<Preference> {
        return flowOf(
            Preference.Plain(
                title = MR.strings.source_code,
                subtitle = localization.getString(MR.strings.source_code_url_short),
                onClick = { urlOpener.open(localization.getString(MR.strings.source_code_url)) },
            )
        )
    }
}