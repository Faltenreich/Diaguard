package com.faltenreich.diaguard.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.food.search.FoodSearchViewModel
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bottom.BottomSheetNavigationScreen
import com.faltenreich.diaguard.navigation.screen
import com.faltenreich.diaguard.navigation.top.TopAppBar
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.architecture.collectAsStateWithLifecycle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = inject(),
    navigation: Navigation = inject(),
) {
    val state = viewModel.collectState()
    if (state !is MainState.Loaded) return

    val snackbarHostState = remember { SnackbarHostState() }
    SideEffect { navigation.snackbarState = snackbarHostState }

    val navController = rememberNavController()
    SideEffect { navigation.navController = navController }

    val currentScreen by navigation.currentScreen.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        Scaffold(
            topBar = {
                val style = currentScreen?.topAppBarStyle ?: TopAppBarStyle.Hidden
                if (style != TopAppBarStyle.Hidden) {
                    TopAppBar(style)
                }
            },
            content = { padding ->
                NavHost(
                    navController = navController,
                    startDestination = DashboardScreen,
                    modifier = Modifier.padding(padding),
                ) {
                    screen<DashboardScreen>(route = { toRoute<DashboardScreen>() }) {
                        Dashboard(viewModel = getViewModel())
                    }
                    screen<EntryFormScreen>(route = { toRoute<EntryFormScreen>() }) { screen ->
                        EntryForm(
                            viewModel = getViewModel {
                                EntryFormViewModel(
                                    entryId = screen.entryId,
                                    dateTimeIsoString = screen.dateTimeIsoString,
                                    foodId = screen.foodId,
                                )
                            },
                            foodSearchViewModel = getViewModel {
                                FoodSearchViewModel(mode = FoodSearchMode.FIND)
                            }
                        )
                    }
                }
            },
            bottomBar = {
                val style = currentScreen?.bottomAppBarStyle ?: BottomAppBarStyle.Hidden
                if (style != BottomAppBarStyle.Hidden) {
                    BottomAppBar(
                        style = style,
                        onMenuClick = {
                            navigation.pushBottomSheet(BottomSheetNavigationScreen)
                        },
                    )
                }
            },
            // FIXME: Overlapped by BottomSheet
            // https://github.com/adrielcafe/voyager/issues/454
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        )

        state.modal?.Content()
    }
}