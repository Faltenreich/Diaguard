package com.faltenreich.diaguard.view

import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier

actual fun Modifier.keyboardPadding(): Modifier {
    return imePadding()
}