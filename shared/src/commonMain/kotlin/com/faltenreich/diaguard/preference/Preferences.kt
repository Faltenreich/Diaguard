package com.faltenreich.diaguard.preference

import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Preferences(
    private val scope: CoroutineScope = inject(),
    private val keyValueStore: KeyValueStore = inject(),
) {

    var startScreen: Flow<Int?>
        get() = keyValueStore.read<Int>(KEY_START_SCREEN)
        set(value) {
            scope.launch { keyValueStore.write(KEY_START_SCREEN, value) }
        }

    companion object {

        private const val KEY_START_SCREEN = "startScreen"
    }
}