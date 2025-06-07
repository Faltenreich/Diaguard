package com.faltenreich.diaguard.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.inject
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType

inline fun <reified T : Screen> NavGraphBuilder.screen(
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline enterTransition:
    (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = null,
    noinline exitTransition:
    (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = null,
    noinline popEnterTransition:
    (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?)? = enterTransition,
    noinline popExitTransition:
    (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?)? = exitTransition,
    noinline sizeTransform:
    (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?)? = null,
    navigation: Navigation = inject(),
) {
    composable<T>(
        typeMap,
        deepLinks,
        enterTransition,
        exitTransition,
        popEnterTransition,
        popExitTransition,
        sizeTransform,
    ) { backStackEntry ->
        val screen = backStackEntry.toRoute<T>()

        navigation.setTopAppBarStyle(screen.TopAppBar())
        navigation.setBottomAppBarStyle(screen.BottomAppBar())

        screen.Content()
    }
}

fun NavController.navigate(screen: Screen, popHistory: Boolean) {
    navigate(
        route = screen,
        navOptions = NavOptions.Builder()
            .run {
                if (popHistory) setPopUpTo(
                    route = graph.findStartDestination().route,
                    inclusive = true,
                )
                else this
            }
            .build()
    )
}