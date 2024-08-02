package com.faltenreich.diaguard.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationIntent
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBar
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.bottomsheet.BottomSheetNavigationScreen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.BottomSheet

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = inject(),
    navigation: Navigation = inject(),
) {
    val state = viewModel.collectState()
    if (state !is MainState.Loaded) return

    val navController = rememberNavController()
    SideEffect { navigation.navController = navController }

    val snackbarHostState = remember { SnackbarHostState() }
    SideEffect { navigation.snackbarState = snackbarHostState }

    Box(modifier = modifier) {
        Scaffold(
            topBar = {
                val style = state.currentScreen?.topAppBarStyle ?: TopAppBarStyle.Hidden
                if (style != TopAppBarStyle.Hidden) {
                    TopAppBar(style)
                }
            },
            content = { padding ->
                MainNavigation(
                    navController = navController,
                    modifier = Modifier.padding(padding),
                )

                val bottomSheet = state.bottomSheet
                if (bottomSheet != null) {
                    BottomSheet(
                        onDismissRequest = { viewModel.dispatchIntent(NavigationIntent.CloseBottomSheet) },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    ) {
                        bottomSheet.Content()
                    }
                }

                state.modal?.Content()
            },
            bottomBar = {
                val style = state.currentScreen?.bottomAppBarStyle ?: BottomAppBarStyle.Hidden
                if (style != BottomAppBarStyle.Hidden) {
                    BottomAppBar(
                        style = style,
                        onMenuClick = {
                            viewModel.dispatchIntent(NavigationIntent.OpenBottomSheet(
                                BottomSheetNavigationScreen
                            ))
                        },
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        )
    }
}