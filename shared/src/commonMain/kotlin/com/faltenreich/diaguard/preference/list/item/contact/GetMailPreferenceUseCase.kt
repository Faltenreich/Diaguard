package com.faltenreich.diaguard.preference.list.item.contact

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.networking.UrlOpener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetMailPreferenceUseCase(
    private val urlOpener: UrlOpener,
    private val localization: Localization,
) {

    operator fun invoke(): Flow<Preference> {
        return flowOf(
            Preference.Plain(
                title = MR.strings.mail,
                subtitle = localization.getString(MR.strings.mail_url_short),
                onClick = { urlOpener.open(localization.getString(MR.strings.mail_url)) },
            )
        )
    }
}