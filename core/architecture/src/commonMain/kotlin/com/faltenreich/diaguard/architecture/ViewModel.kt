package com.faltenreich.diaguard.architecture

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faltenreich.diaguard.injection.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * @param State represents a view state for the related Composable
 * @param Intent represents one-shot actions directed from the Composable to the ViewModel
 * @param Event represents one-shot actions directed from the ViewModel to the Composable
 */
abstract class ViewModel<State, Intent, Event>(
    private val dispatcher: CoroutineDispatcher = inject(),
) : ViewModel() {

    val scope: CoroutineScope
        get() = viewModelScope

    abstract val state: Flow<State>

    private val stateInScope: StateFlow<State?> by lazy {
        state.flowOn(dispatcher).stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )
    }

    val events = MutableSharedFlow<Event>(replay = 1)

    @Composable
    fun collectState(): State? {
        return stateInScope.collectAsStateWithLifecycle(dispatcher).value
    }

    /**
     * Handles [intent] on [dispatcher] within [scope]
     *
     * Caution: Causes race conditions for text inputs, use [handleIntent] instead
     */
    fun dispatchIntent(intent: Intent) {
        scope.launch(dispatcher) {
            handleIntent(intent)

            intent?.let {
                // Logger.debug("Dispatched intent: ${intent::class.simpleName}")
            }
        }
    }

    /**
     * Handles [intent] on current dispatcher
     */
    open suspend fun handleIntent(intent: Intent) = Unit

    /**
     * Posts [event] asynchronously via [dispatcher] within [scope]
     */
    fun postEvent(event: Event) {
        scope.launch(dispatcher) {
            events.emit(event)

            event?.let {
                // Logger.debug("Posted event: ${event::class.simpleName}")
            }
        }
    }

    /**
     * Collects one-shot [events]
     */
    suspend fun collectEvents(onEvent: (Event) -> Unit) {
        events.collect { event ->
            onEvent(event)
            events.resetReplayCache()
        }
    }
}