package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.injection.sharedViewModel
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitSelectionEvent
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitSelectionViewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.ic_check
import com.faltenreich.diaguard.resource.ic_delete
import com.faltenreich.diaguard.resource.measurement_property
import com.faltenreich.diaguard.resource.measurement_property_delete
import com.faltenreich.diaguard.resource.save
import com.faltenreich.diaguard.view.bar.BottomAppBarItem
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class MeasurementPropertyFormScreen(
    private val categoryId: Long,
    private val propertyId: Long?,
) : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.measurement_property))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<MeasurementPropertyFormViewModel> { parametersOf(categoryId, propertyId) }
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = stringResource(Res.string.measurement_property_delete),
                    onClick = {
                        viewModel.dispatchIntent(
                            MeasurementPropertyFormIntent.Delete(
                                needsConfirmation = true
                            )
                        )
                    },
                )
            },
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(Res.drawable.ic_check),
                    contentDescription = stringResource(Res.string.save),
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.Submit) },
                )
            },
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<MeasurementPropertyFormViewModel> {
            parametersOf(categoryId, propertyId)
        }
        val unitSelectionViewModel = sharedViewModel<MeasurementUnitSelectionViewModel>()
        LaunchedEffect(Unit) {
            unitSelectionViewModel.collectEvents { event ->
                when (event) {
                    is MeasurementUnitSelectionEvent.Select ->
                        viewModel.dispatchIntent(MeasurementPropertyFormIntent.SelectUnit(event.unit))
                }
            }
        }
        MeasurementPropertyForm(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}