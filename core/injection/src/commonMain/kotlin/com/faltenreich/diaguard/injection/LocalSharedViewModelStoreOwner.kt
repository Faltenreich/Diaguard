package com.faltenreich.diaguard.injection

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelStoreOwner

val LocalSharedViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    throw IllegalStateException("LocalSharedViewModelStoreOwner not yet set")
}