package com.faltenreich.diaguard.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bottom.BottomSheetNavigationScreen
import com.faltenreich.diaguard.navigation.top.TopAppBar
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = inject(),
    navigation: Navigation = inject(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val state = viewModel.collectState()
    if (state !is MainState.Loaded) return

    SideEffect { navigation.snackbarState = snackbarHostState }

    // TODO: Migrate to Material3
    // https://github.com/adrielcafe/voyager/issues/185
    BottomSheetNavigator(
        modifier = modifier,
        hideOnBackPress = true,
        scrimColor = BottomSheetDefaults.ScrimColor,
        sheetShape = BottomSheetDefaults.ExpandedShape,
        sheetElevation = BottomSheetDefaults.Elevation,
        sheetBackgroundColor = BottomSheetDefaults.ContainerColor,
    ) { bottomSheetNavigator ->
        SideEffect { navigation.bottomSheetNavigator = bottomSheetNavigator }

        Navigator(screen = state.startScreen) { navigator ->
            SideEffect { navigation.navigator = navigator }

            Box {
                Scaffold(
                    topBar = {
                        val screen = navigator.lastItem as? Screen
                        val style = screen?.topAppBarStyle ?: TopAppBarStyle.Hidden
                        if (style != TopAppBarStyle.Hidden) {
                            TopAppBar(style)
                        }
                    },
                    content = { padding ->
                        Box(modifier = Modifier.padding(padding)) {
                            CurrentScreen()
                        }
                    },
                    bottomBar = {
                        val screen = navigator.lastItem as? Screen
                        val style = screen?.bottomAppBarStyle ?: BottomAppBarStyle.Hidden
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
    }
}