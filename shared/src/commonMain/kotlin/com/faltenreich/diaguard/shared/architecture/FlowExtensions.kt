/**
 * Workaround for: Duplicate JVM class name
 * https://youtrack.jetbrains.com/issue/KT-21186
 */
@file:JvmName("FlowExtensionsJvm")

package com.faltenreich.diaguard.shared.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmName

@Composable
expect fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    context: CoroutineContext = EmptyCoroutineContext,
): State<T>