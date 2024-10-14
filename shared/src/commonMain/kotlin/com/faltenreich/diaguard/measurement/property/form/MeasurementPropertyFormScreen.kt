package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.measurement_property
import diaguard.shared.generated.resources.measurement_property_delete
import diaguard.shared.generated.resources.save
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

@Serializable
data class MeasurementPropertyFormScreen(val propertyId: Long) : Screen {

    constructor(property: MeasurementProperty.Local) : this(propertyId = property.id)

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.measurement_property))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<MeasurementPropertyFormViewModel> { parametersOf(propertyId) }
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.measurement_property_delete,
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.DeleteProperty) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.UpdateProperty) },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.save),
                    )
                }
            },
        )
    }

    @Composable
    override fun Content() {
        MeasurementPropertyForm(viewModel = viewModel { parametersOf(propertyId) })
    }
}