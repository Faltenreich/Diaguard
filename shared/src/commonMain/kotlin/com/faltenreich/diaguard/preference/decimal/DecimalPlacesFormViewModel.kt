package com.faltenreich.diaguard.preference.decimal

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.flowOf

class DecimalPlacesFormViewModel : ViewModel<Unit, Unit, Unit>() {

    override val state = flowOf(Unit)
}