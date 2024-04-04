package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyForm
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormIntent
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import dev.icerock.moko.resources.compose.painterResource
import org.koin.core.parameter.parametersOf

data class MeasurementPropertyFormScreen(val property: MeasurementProperty) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.measurement_property))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<MeasurementPropertyFormViewModel> { parametersOf(property) }
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_delete),
                    contentDescription = MR.strings.measurement_property_delete,
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.DeleteProperty) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementPropertyFormViewModel> { parametersOf(property) }
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.UpdateProperty) },
                ) {
                    Icon(
                        painter = painterResource(MR.images.ic_check),
                        contentDescription = getString(MR.strings.save),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        MeasurementPropertyForm(viewModel = getViewModel { parametersOf(property) })
    }
}