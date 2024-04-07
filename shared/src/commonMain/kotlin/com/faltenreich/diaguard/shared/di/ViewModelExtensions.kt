package com.faltenreich.diaguard.shared.di

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@Composable
inline fun <reified T : ViewModel<*, *, *>> Screen.getViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    return getScreenModel(qualifier, parameters)
}

@Suppress("UnusedReceiverParameter")
@Composable
inline fun <reified T : ViewModel<*, *, *>> Screen.getSharedViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    return LocalNavigator.currentOrThrow.getNavigatorScreenModel(qualifier, parameters)
}