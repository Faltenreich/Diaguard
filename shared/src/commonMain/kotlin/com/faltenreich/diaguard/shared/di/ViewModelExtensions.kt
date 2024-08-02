/**
 * Workaround for: Duplicate JVM class name
 * https://youtrack.jetbrains.com/issue/KT-21186
 */
@file:JvmName("ViewModelExtensionsJvm")

package com.faltenreich.diaguard.shared.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.currentKoinScope
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.viewmodel.defaultExtras
import kotlin.jvm.JvmName

@Composable
inline fun <reified T : ViewModel> viewModel(
    qualifier: Qualifier? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    extras: CreationExtras = defaultExtras(viewModelStoreOwner),
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null,
): T {
    return koinViewModel(qualifier, viewModelStoreOwner, key, extras, scope, parameters)
}

@Deprecated("Use Koin instead", ReplaceWith("viewModel"))
@Composable
inline fun <reified T : ViewModel> getViewModel(
    owner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
    key: String? = null,
    noinline initializer: CreationExtras.() -> T,
): T {
    return viewModel(owner, key, initializer)
}

@Deprecated("Use Koin instead", ReplaceWith("viewModel"))
@Composable
inline fun <reified T : ViewModel> getViewModel(
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
inline fun <reified T : ViewModel> getSharedViewModel(
    noinline initializer: CreationExtras.() -> T,
): T {
    TODO()
}