package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyList
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyListIntent
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyListViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.measurement_properties
import diaguard.shared.generated.resources.measurement_property_new
import org.jetbrains.compose.resources.painterResource

data object MeasurementPropertyListScreen : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.measurement_properties))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementPropertyListViewModel>()
                FloatingActionButton(
                    onClick = {
                        viewModel.dispatchIntent(MeasurementPropertyListIntent.Create)
                    },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.measurement_property_new),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        MeasurementPropertyList(viewModel = getViewModel())
    }
}