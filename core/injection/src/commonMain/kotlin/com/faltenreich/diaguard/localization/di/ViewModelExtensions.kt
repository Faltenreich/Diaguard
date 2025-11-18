@file:JvmName("ViewModelExtensionsCommon")

package com.faltenreich.diaguard.localization.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.ParametersDefinition
import kotlin.jvm.JvmName

@Composable
inline fun <reified T : ViewModel> viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
    noinline parameters: ParametersDefinition? = null,
): T {
    return koinViewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        parameters = parameters,
    )
}

@Composable
inline fun <reified T : ViewModel> sharedViewModel(
    noinline parameters: ParametersDefinition? = null,
): T {
    return viewModel(
        viewModelStoreOwner = LocalSharedViewModelStoreOwner.current,
        parameters = parameters,
    )
}

@Composable
expect fun rememberViewModelStoreOwner(): ViewModelStoreOwner