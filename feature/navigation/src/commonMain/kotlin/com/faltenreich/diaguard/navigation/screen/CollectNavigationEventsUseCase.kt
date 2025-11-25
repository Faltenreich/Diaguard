package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow

class CollectNavigationEventsUseCase(private val navigation: Navigation) {

    suspend operator fun invoke(onEvent: FlowCollector<NavigationEvent>): SharedFlow<NavigationEvent> {
        navigation.events.collect(onEvent)
    }
}