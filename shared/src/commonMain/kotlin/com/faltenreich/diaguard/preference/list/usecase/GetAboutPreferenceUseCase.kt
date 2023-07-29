package com.faltenreich.diaguard.preference.list.usecase

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetAboutPreferenceUseCase {

    operator fun invoke(): Flow<Preference> {
        val preference = Preference.Category(
            title = { stringResource(MR.strings.about) },
            icon = MR.images.ic_about,
        )
        return flowOf(preference)
    }
}