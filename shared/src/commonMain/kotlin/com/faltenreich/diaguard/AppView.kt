package com.faltenreich.diaguard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyleFactory
import com.faltenreich.diaguard.navigation.bottom.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyleFactory
import com.faltenreich.diaguard.shared.view.BottomSheetState
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

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
                            when (val style = TopAppBarStyleFactory.forScreen(screen)) {
                                is TopAppBarStyle.Hidden -> Unit
                                is TopAppBarStyle.CenterAligned -> CenterAlignedTopAppBar(
                                    title = { style.content() },
                                    navigationIcon = {
                                        if (navigator.canPop) {
                                            IconButton(onClick = navigator::pop) {
                                                Icon(
                                                    Icons.Filled.ArrowBack,
                                                    contentDescription = stringResource(MR.strings.navigate_back),
                                                )
                                            }
                                        }
                                    }
                                )
                            }
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
                            when (val style = BottomAppBarStyleFactory.forScreen(screen)) {
                                is BottomAppBarStyle.Hidden -> Unit
                                is BottomAppBarStyle.Visible -> {
                                    BottomAppBar(
                                        actions = {
                                            BottomAppBarItem(
                                                image = Icons.Filled.Menu,
                                                contentDescription = MR.strings.menu_open,
                                                onClick = { scope.launch { sheetState.show() } },
                                            )
                                            style.actions()
                                        },
                                        floatingActionButton = { style.floatingActionButton() },
                                    )
                                }
                            }
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