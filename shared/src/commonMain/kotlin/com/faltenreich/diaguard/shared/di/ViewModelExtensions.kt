package com.faltenreich.diaguard.shared.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faltenreich.diaguard.shared.architecture.ViewModel

@Composable
inline fun <reified T : ViewModel<*, *, *>> getViewModel(
    owner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
    key: String? = null,
    noinline initializer: CreationExtras.() -> T,
): T {
    return viewModel(owner, key, initializer)
}

@Composable
inline fun <reified T : ViewModel<*, *, *>> getViewModel(
    owner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extras: CreationExtras =
        if (owner is HasDefaultViewModelProviderFactory) owner.defaultViewModelCreationExtras
        else CreationExtras.Empty,
): T {
    return viewModel(owner, key, factory, extras)
}

@Composable
inline fun <reified T : ViewModel<*, *, *>> getSharedViewModel(): T {
    TODO()
}