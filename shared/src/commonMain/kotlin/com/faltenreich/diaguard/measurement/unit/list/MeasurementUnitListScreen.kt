package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.sharedViewModel
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import diaguard.core.view.generated.resources.ic_add
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_unit_new
import diaguard.shared.generated.resources.measurement_units
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class MeasurementUnitListScreen(private val modeOrdinal: Int) : Screen {

    constructor(mode: MeasurementUnitListMode) : this(modeOrdinal = mode.ordinal)

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.measurement_units))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<MeasurementUnitListViewModel>(
            parameters = {
                parametersOf(MeasurementUnitListMode.entries.first { it.ordinal == modeOrdinal })
            },
        )
        return BottomAppBarStyle.Visible(
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(diaguard.core.view.generated.resources.Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.measurement_unit_new),
                    onClick = { viewModel.dispatchIntent(MeasurementUnitListIntent.OpenFormDialog()) },
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<MeasurementUnitListViewModel>(
            parameters = {
                parametersOf(MeasurementUnitListMode.entries.first { it.ordinal == modeOrdinal })
            },
        )
        val selectionViewModel = sharedViewModel<MeasurementUnitSelectionViewModel>()
        MeasurementUnitList(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
            onSelect = { unit ->
                when (viewModel.mode) {
                    MeasurementUnitListMode.STROLL -> {
                        viewModel.dispatchIntent(MeasurementUnitListIntent.OpenFormDialog(unit))
                    }
                    MeasurementUnitListMode.FIND -> {
                        selectionViewModel.postEvent(MeasurementUnitSelectionEvent.Select(unit))
                        viewModel.dispatchIntent(MeasurementUnitListIntent.Close)
                    }
                }
            }
        )
    }
}