package com.faltenreich.diaguard.preference.list.item.contact

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.preference.list.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetContactPreferenceUseCase {

    operator fun invoke(): Flow<Preference> {
        return flowOf(
            Preference.Category(
                title = MR.strings.contact,
                icon = MR.images.ic_contact,
            )
        )
    }
}