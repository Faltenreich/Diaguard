package com.faltenreich.diaguard.preference.list.item.data

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.preference.list.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetMeasurementPreferenceUseCase {

    operator fun invoke(): Flow<Preference> {
        return flowOf(
            Preference.Plain(
                title = MR.strings.measurement_properties,
                subtitle = null,
                onClick = { navigator -> navigator.push(Screen.MeasurementPropertyList) },
            )
        )
    }
}