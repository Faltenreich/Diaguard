package com.faltenreich.diaguard.preference.list.item.about

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetLicensesPreferenceUseCase {

    operator fun invoke(): Flow<Preference> {
        return flowOf(
            Preference.Plain(
                title = MR.strings.licenses,
                subtitle = null,
                onClick = { TODO() },
            )
        )
    }
}