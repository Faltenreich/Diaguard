package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier

actual fun Modifier.keyboardPadding(): Modifier {
    return imePadding()
}