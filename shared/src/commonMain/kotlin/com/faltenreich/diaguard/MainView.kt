package com.faltenreich.diaguard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bottom.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.bottom.bottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBar
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.top.topAppBarStyle
import com.faltenreich.diaguard.shared.view.keyboardPadding
import com.faltenreich.diaguard.shared.view.rememberBottomSheetState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainView(
    modifier: Modifier = Modifier,
) {
    AppTheme {
        Surface (
            modifier = modifier.fillMaxSize().keyboardPadding(),
            color = AppTheme.colors.material.background,
        ) {
            // TODO: Get start screen from preferences
            Navigator(screen = Screen.Log()) { navigator ->
                Box {
                    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
                    val bottomSheetState = rememberBottomSheetState()
                    Scaffold(
                        topBar = {
                            val screen = navigator.lastItem as? Screen
                            val style = screen?.topAppBarStyle()
                            AnimatedVisibility(style != null && style != TopAppBarStyle.Hidden) {
                                style ?: return@AnimatedVisibility
                                TopAppBar(
                                    style = style,
                                    navigator = navigator,
                                )
                            }
                        },
                        content = { padding ->
                            FadeTransition(
                                navigator = navigator,
                                modifier = Modifier.padding(padding),
                            )
                        },
                        bottomBar = {
                            val screen = navigator.lastItem as? Screen
                            val style = screen?.bottomAppBarStyle()
                            AnimatedVisibility(style != null && style != BottomAppBarStyle.Hidden) {
                                style ?: return@AnimatedVisibility
                                BottomAppBar(
                                    style = style,
                                    onMenuClick = { openBottomSheet = true },
                                )
                            }
                        },
                    )
                    if (openBottomSheet) {
                        BottomSheetNavigation(
                            bottomSheetState = bottomSheetState,
                            navigator = navigator,
                            onDismissRequest = { openBottomSheet = false },
                        )
                    }
                }
            }
        }
    }
}