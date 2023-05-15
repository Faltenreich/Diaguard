package com.faltenreich.diaguard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bottom.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.bottom.bottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBar
import com.faltenreich.diaguard.navigation.top.topAppBarStyle
import com.faltenreich.diaguard.shared.view.BottomSheetState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppView() {
    AppTheme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = AppTheme.colorScheme.background,
        ) {
            Navigator(screen = Screen.Log()) { navigator ->
                val scope = rememberCoroutineScope()
                val sheetState = remember { BottomSheetState() }
                Box {
                    Scaffold(
                        topBar = {
                            val screen = navigator.lastItem as? Screen ?: return@Scaffold
                            TopAppBar(
                                style = screen.topAppBarStyle(),
                                navigator = navigator,
                            )
                        },
                        content = { padding ->
                            // FIXME: Crashes on pushing same Screen twice
                            FadeTransition(
                                navigator = navigator,
                                modifier = Modifier.padding(padding),
                            )
                        },
                        bottomBar = {
                            val screen = navigator.lastItem as? Screen ?: return@Scaffold
                            BottomAppBar(
                                style = screen.bottomAppBarStyle(),
                                sheetState = sheetState,
                                scope = scope,
                            )
                        },
                    )
                    if (sheetState.isVisible) {
                        // FIXME: Wrap inside BottomSheetScaffold or ModalBottomSheetLayout to fix broken animations caused by nested Composables
                        BottomSheetNavigation(sheetState = sheetState)
                    }
                }
            }
        }
    }
}