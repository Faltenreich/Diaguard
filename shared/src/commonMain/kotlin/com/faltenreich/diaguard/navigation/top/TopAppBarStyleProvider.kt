package com.faltenreich.diaguard.navigation.top

import androidx.compose.material3.Text
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Screen
import dev.icerock.moko.resources.compose.stringResource

fun Screen.topAppBarStyle(): TopAppBarStyle {
    return when (this) {
        is Screen.EntryForm -> TopAppBarStyle.CenterAligned {
            Text(stringResource(MR.strings.entry))
        }
        is Screen.MeasurementPropertyList -> TopAppBarStyle.CenterAligned {
            Text(stringResource(MR.strings.measurement_properties))
        }
        is Screen.MeasurementPropertyForm -> TopAppBarStyle.CenterAligned {
            Text(stringResource(MR.strings.measurement_property))
        }
        is Screen.MeasurementTypeForm -> TopAppBarStyle.CenterAligned {
            Text(stringResource(MR.strings.measurement_type))
        }
        is Screen.PreferenceList -> TopAppBarStyle.CenterAligned {
            Text(stringResource(MR.strings.preferences))
        }
        else -> TopAppBarStyle.Hidden
    }
}