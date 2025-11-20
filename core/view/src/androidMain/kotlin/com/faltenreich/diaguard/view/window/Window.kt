package com.faltenreich.diaguard.view.window

import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier

actual fun Modifier.keyboardPadding(): Modifier {
    return imePadding()
}