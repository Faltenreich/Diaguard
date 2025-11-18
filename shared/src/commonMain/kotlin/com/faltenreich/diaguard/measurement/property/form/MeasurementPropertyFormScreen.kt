package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.faltenreich.diaguard.localization.di.sharedViewModel
import com.faltenreich.diaguard.localization.di.viewModel
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitSelectionEvent
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitSelectionViewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.measurement_property
import diaguard.shared.generated.resources.measurement_property_delete
import diaguard.shared.generated.resources.save
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class MeasurementPropertyFormScreen(
    val categoryId: Long,
    val propertyId: Long?,
) : Screen {

    constructor(category: MeasurementCategory.Local) : this(
        categoryId = category.id,
        propertyId = null,
    )

    constructor(property: MeasurementProperty.Local) : this(
        categoryId = property.category.id,
        propertyId = property.id,
    )

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
                        viewModel.dispatchIntent(MeasurementPropertyFormIntent.Delete(needsConfirmation = true))
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
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