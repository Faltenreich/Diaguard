package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.emptyFlow

class NavigationViewModel : ViewModel<Unit, Unit, NavigationEvent>() {

    override val state = emptyFlow<Unit>()
}