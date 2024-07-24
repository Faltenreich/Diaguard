package com.faltenreich.diaguard.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

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

@Suppress("UnusedReceiverParameter")
@Composable
inline fun <reified T : ViewModel<*, *, *>> Screen.getSharedViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null,
): T {
    val state = parameters?.let { rememberUpdatedState(parameters) }
    val tag = remember(qualifier, scope) { qualifier?.value }
    return LocalNavigator.currentOrThrow.rememberNavigatorScreenModel(tag = tag) {
        scope.get(qualifier, state?.value)
    }
}