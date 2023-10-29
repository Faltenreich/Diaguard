package com.faltenreich.diaguard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.faltenreich.diaguard.navigation.NavigationViewModel
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bottom.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.bottom.bottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBar
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.top.topAppBarStyle
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.rememberBottomSheetState

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    navigationViewModel: NavigationViewModel = inject(),
) {
    val viewState = navigationViewModel.viewState.collectAsState().value
    val startScreen = viewState.startScreen ?: return
    Box(modifier = modifier) {
        Navigator(screen = startScreen) { navigator ->
            Box {
                var openBottomSheet by rememberSaveable { mutableStateOf(false) }
                val bottomSheetState = rememberBottomSheetState()
                Scaffold(
                    topBar = {
                        val screen = navigator.lastItem as? Screen
                        val style = screen?.topAppBarStyle() ?: TopAppBarStyle.Hidden
                        AnimatedVisibility(style != TopAppBarStyle.Hidden) {
                            // TODO: Extract to prevent jumping during transition
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
                        val style = screen?.bottomAppBarStyle() ?: BottomAppBarStyle.Hidden
                        AnimatedVisibility(style != BottomAppBarStyle.Hidden) {
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