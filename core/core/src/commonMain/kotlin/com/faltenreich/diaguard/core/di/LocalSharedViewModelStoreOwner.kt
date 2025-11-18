package com.faltenreich.diaguard.core.di

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelStoreOwner

val LocalSharedViewModelStoreOwner = compositionLocalOf<ViewModelStoreOwner> {
    throw IllegalStateException("LocalSharedViewModelStoreOwner not yet set")
}