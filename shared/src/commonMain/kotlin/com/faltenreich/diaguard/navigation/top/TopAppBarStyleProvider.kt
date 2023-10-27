package com.faltenreich.diaguard.navigation.top

import androidx.compose.material3.Text
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.localization.getString

fun Screen.topAppBarStyle(): TopAppBarStyle {
    return when (this) {
        is Screen.EntryForm -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.entry))
        }
        is Screen.MeasurementPropertyList -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.measurement_properties))
        }
        is Screen.MeasurementPropertyForm -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.measurement_property))
        }
        is Screen.MeasurementTypeForm -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.measurement_type))
        }
        is Screen.ExportForm -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.export))
        }
        is Screen.PreferenceList -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.preferences))
        }
        else -> TopAppBarStyle.Hidden
    }
}