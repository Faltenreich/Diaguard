package com.faltenreich.diaguard.food.search

import com.faltenreich.diaguard.architecture.ViewModel
import kotlinx.coroutines.flow.emptyFlow

class FoodSelectionViewModel : ViewModel<Unit, Unit, FoodSelectionEvent>() {

    override val state = emptyFlow<Unit>()
}