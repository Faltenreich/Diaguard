package com.faltenreich.rhyme.shared.architecture

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*

actual abstract class ViewModel {

    private var hasCleared = false

    actual val viewModelScope: CoroutineScope by lazy {
        val result = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        if (hasCleared) {
            closeWithRuntimeException(result)
        }
        result
    }

    protected actual open fun onCleared() {}

    fun clear() {
        hasCleared = true
        closeWithRuntimeException(viewModelScope)
        onCleared()
    }

    companion object {

        private fun closeWithRuntimeException(obj: Any?) {
            if (obj is Closeable) {
                try {
                    obj.close()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}