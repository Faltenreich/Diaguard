package com.faltenreich.diaguard.measurement.unit.form

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.shared.di.viewModel
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf

@Serializable
data class MeasurementUnitFormModal(private val unitId: Long? = null) : Modal {

    @Composable
    override fun Content() {
        MeasurementUnitForm(viewModel = viewModel { parametersOf(unitId) })
    }
}