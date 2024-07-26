package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.measurement_property
import diaguard.shared.generated.resources.measurement_property_delete
import diaguard.shared.generated.resources.save
import org.jetbrains.compose.resources.painterResource

data class MeasurementPropertyFormScreen(val property: MeasurementProperty.Local) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.measurement_property))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<MeasurementPropertyFormViewModel> { MeasurementPropertyFormViewModel(property) }
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.measurement_property_delete,
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.DeleteProperty) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementPropertyFormViewModel> { MeasurementPropertyFormViewModel(property) }
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

    @Composable
    override fun Content() {
        MeasurementPropertyForm(viewModel = getViewModel { MeasurementPropertyFormViewModel(property) })
    }
}