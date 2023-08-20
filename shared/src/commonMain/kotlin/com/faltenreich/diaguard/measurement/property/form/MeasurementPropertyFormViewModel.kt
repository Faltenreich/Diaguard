package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.architecture.ViewModel

class MeasurementPropertyFormViewModel(
    property: MeasurementProperty,
) : ViewModel() {

    private val id: Long = property.id

    var name by mutableStateOf<String>(property.name)
    var icon by mutableStateOf<String>(property.icon ?: "")
}