package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_units
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
object MeasurementUnitListScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.measurement_units))
        }
    }

    @Composable
    override fun Content() {
        MeasurementUnitList(viewModel = viewModel())
    }
}