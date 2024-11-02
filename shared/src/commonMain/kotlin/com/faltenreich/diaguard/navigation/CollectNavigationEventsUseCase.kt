package com.faltenreich.diaguard.navigation

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow

class CollectNavigationEventsUseCase(private val navigation: Navigation) {

    suspend operator fun invoke(onEvent: FlowCollector<NavigationEvent>): SharedFlow<NavigationEvent> {
        navigation.events.collect(onEvent)
    }
}