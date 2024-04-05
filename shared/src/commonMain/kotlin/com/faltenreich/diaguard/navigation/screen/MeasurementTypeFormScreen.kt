package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeForm
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormIntent
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

data class MeasurementTypeFormScreen(val type: MeasurementType) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.measurement_type))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<MeasurementTypeFormViewModel> { parametersOf(type) }
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.measurement_type_delete,
                    onClick = { viewModel.dispatchIntent(MeasurementTypeFormIntent.DeleteType) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementTypeFormViewModel> { parametersOf(type) }
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(MeasurementTypeFormIntent.UpdateType) },
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
        MeasurementTypeForm(viewModel = getViewModel { parametersOf(type) })
    }
}