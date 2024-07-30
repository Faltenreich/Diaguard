/**
 * Workaround for: Duplicate JVM class name
 * https://youtrack.jetbrains.com/issue/KT-21186
 */
@file:JvmName("ViewModelExtensionsJvm")

package com.faltenreich.diaguard.shared.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlin.jvm.JvmName

@Deprecated("Use Koin instead", ReplaceWith("viewModel"))
@Composable
inline fun <reified T : ViewModel<*, *, *>> getViewModel(
    owner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
    key: String? = null,
    noinline initializer: CreationExtras.() -> T,
): T {
    return viewModel(owner, key, initializer)
}

@Deprecated("Use Koin instead", ReplaceWith("viewModel"))
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

@Deprecated("Use Koin instead", ReplaceWith("activityViewModel"))
@Composable
inline fun <reified T : ViewModel<*, *, *>> getSharedViewModel(
    noinline initializer: CreationExtras.() -> T,
): T {
    TODO()
}