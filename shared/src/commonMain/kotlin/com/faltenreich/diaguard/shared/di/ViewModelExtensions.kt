package com.faltenreich.diaguard.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

/**
 * Wrapper for retrieving [ViewModel] for [Screen]
 *
 * Causing java.lang.ClassNotFoundException:
 * Didn't find class "org.koin.compose.stable.StableHoldersKt"
 *
 * Issues:
 * https://github.com/InsertKoinIO/koin/issues/1879
 * https://github.com/adrielcafe/voyager/issues/443
 *
 * Workaround:
 * https://github.com/InsertKoinIO/koin/issues/1879#issuecomment-2132926268
 */

@Composable
inline fun <reified T : ViewModel<*, *, *>> Screen.getViewModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null,
): T {
    val state = parameters?.let { rememberUpdatedState(parameters) }
    val tag = remember(qualifier, scope) { qualifier?.value }
    return rememberScreenModel(tag = tag) {
        scope.get(qualifier, state?.value)
    }
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