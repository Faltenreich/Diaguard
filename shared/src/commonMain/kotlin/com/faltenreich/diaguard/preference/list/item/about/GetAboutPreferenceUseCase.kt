package com.faltenreich.diaguard.preference.list.item.about

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAboutPreferenceUseCase {

    operator fun invoke(): Flow<Preference> {
        return flowOf(
            Preference.Category(
                title = MR.strings.about,
                icon = MR.images.ic_about,
            )
        )
    }
}