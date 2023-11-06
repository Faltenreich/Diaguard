package com.faltenreich.diaguard.preference.list.item.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetDataPreferenceUseCase {

    operator fun invoke(): Flow<Preference> {
        return flowOf(
            Preference.Category(
                title = MR.strings.data,
                icon = MR.images.ic_data,
            )
        )
    }
}