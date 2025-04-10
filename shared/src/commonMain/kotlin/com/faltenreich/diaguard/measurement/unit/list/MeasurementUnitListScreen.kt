package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.measurement_unit_new
import diaguard.shared.generated.resources.measurement_units
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
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
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<MeasurementUnitListViewModel>()
        return BottomAppBarStyle.Visible(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(MeasurementUnitListIntent.Create) },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.measurement_unit_new),
                    )
                }
            }
        )
    }

    @Composable
    override fun Content() {
        MeasurementUnitList(viewModel = viewModel())
    }
}