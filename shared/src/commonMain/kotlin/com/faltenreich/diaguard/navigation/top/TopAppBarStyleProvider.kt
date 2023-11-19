package com.faltenreich.diaguard.navigation.top

import androidx.compose.material3.Text
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.ExportFormScreen
import com.faltenreich.diaguard.navigation.screen.FoodDetailScreen
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyListScreen
import com.faltenreich.diaguard.navigation.screen.MeasurementTypeFormScreen
import com.faltenreich.diaguard.navigation.screen.PreferenceListScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.StatisticScreen
import com.faltenreich.diaguard.shared.localization.getString

fun Screen.topAppBarStyle(): TopAppBarStyle {
    return when (this) {
        is DashboardScreen -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.app_name))
        }
        is EntryFormScreen -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.entry))
        }
        is MeasurementPropertyListScreen -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.measurement_properties))
        }
        is MeasurementPropertyFormScreen -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.measurement_property))
        }
        is MeasurementTypeFormScreen -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.measurement_type))
        }
        is FoodListScreen -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.food))
        }
        is FoodDetailScreen -> TopAppBarStyle.CenterAligned {
            Text(food.name)
        }
        is StatisticScreen -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.statistic))
        }
        is ExportFormScreen -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.export))
        }
        is PreferenceListScreen -> TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.preferences))
        }
        else -> TopAppBarStyle.Hidden
    }
}