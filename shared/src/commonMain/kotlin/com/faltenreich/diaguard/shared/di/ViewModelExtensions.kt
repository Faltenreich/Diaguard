package com.faltenreich.diaguard.shared.di

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

@Composable
inline fun <reified T : ViewModel<*, *, *>> Screen.getViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null,
): T {
    return koinScreenModel(qualifier, scope, parameters)
}

@Suppress("UnusedReceiverParameter")
@Composable
inline fun <reified T : ViewModel<*, *, *>> Screen.getSharedViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null,
): T {
    return LocalNavigator.currentOrThrow.koinNavigatorScreenModel(qualifier, scope, parameters)
}