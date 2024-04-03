package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
fun rememberFocusRequester(requestFocus: Boolean = false): FocusRequester {
    val focusRequester = remember { FocusRequester() }
    if (requestFocus) {
        val windowInfo = LocalWindowInfo.current
        LaunchedEffect(windowInfo) {
            // Fixes IllegalStateException on orientation change
            snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
                if (isWindowFocused) {
                    focusRequester.requestFocus()
                }
            }
        }
    }
    return focusRequester
}