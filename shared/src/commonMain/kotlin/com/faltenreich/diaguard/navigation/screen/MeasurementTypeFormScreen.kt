package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeForm
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormIntent
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import dev.icerock.moko.resources.compose.painterResource
import org.koin.core.parameter.parametersOf

data class MeasurementTypeFormScreen(val measurementType: MeasurementType) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.measurement_type))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<MeasurementTypeFormViewModel> { parametersOf(measurementType) }
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_delete),
                    contentDescription = MR.strings.measurement_type_delete,
                    onClick = { viewModel.dispatchIntent(MeasurementTypeFormIntent.ShowDeletionDialog) },
                )
            },
        )

    @Composable
    override fun Content() {
        MeasurementTypeForm(viewModel = getViewModel { parametersOf(measurementType) })
    }
}