package com.faltenreich.diaguard.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bottom.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.top.TopAppBar
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = inject(),
    // TODO: Move into MainViewModel
    navigation: Navigation = inject(),
) {
    val snackbarHostState = remember { SnackbarHostState().also { navigation.snackbarState = it } }
    val modal = navigation.modal.collectAsState().value

    val state = viewModel.collectState()
    if (state !is MainState.Loaded) return

    Navigator(screen = state.startScreen) { navigator ->
        navigation.navigator = navigator

        Box(modifier = modifier) {
            var openBottomSheet by rememberSaveable { mutableStateOf(false) }
            val bottomSheetState = rememberModalBottomSheetState()
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
                        BottomAppBar(style, onMenuClick = { openBottomSheet = true })
                    }
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            )

            if (openBottomSheet) {
                BottomSheetNavigation(
                    bottomSheetState,
                    onDismissRequest = { openBottomSheet = false })
            }

            modal?.Content()
        }
    }
}