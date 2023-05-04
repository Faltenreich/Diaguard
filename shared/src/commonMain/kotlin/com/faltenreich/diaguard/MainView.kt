package com.faltenreich.diaguard

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.navigation.BottomAppBarItem
import com.faltenreich.diaguard.navigation.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.LogTarget
import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.navigation.TopAppBarStyle
import com.faltenreich.diaguard.shared.view.BottomSheetState
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@Composable
fun MainView() {
    MainTheme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Navigator(screen = LogTarget()) { navigator ->
                val scope = rememberCoroutineScope()
                val sheetState = remember { BottomSheetState() }
                Box {
                    Scaffold(
                        topBar = {
                            val navigationTarget = navigator.lastItem as? NavigationTarget ?: return@Scaffold
                            when (val style = navigationTarget.topAppBarStyle) {
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
                            Box(modifier = Modifier.padding(padding)) {
                                CurrentScreen()
                            }
                        },
                        bottomBar = {
                            val navigationTarget = navigator.lastItem as? NavigationTarget ?: return@Scaffold
                            when (val style = navigationTarget.bottomAppBarStyle) {
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