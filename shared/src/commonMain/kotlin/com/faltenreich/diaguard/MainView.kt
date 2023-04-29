package com.faltenreich.diaguard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import cafe.adriel.voyager.transitions.SlideTransition
import com.faltenreich.diaguard.navigation.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.DashboardTarget
import com.faltenreich.diaguard.navigation.EntryFormTarget
import com.faltenreich.diaguard.shared.view.BottomSheetState
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainView() {
    MainTheme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Navigator(screen = DashboardTarget) { navigator ->
                val scope = rememberCoroutineScope()
                val sheetState = remember { BottomSheetState() }
                // FIXME: Recomposes more than once initially
                SlideTransition(navigator) {
                    Box {
                        Scaffold(
                            content = { CurrentScreen() },
                            bottomBar = {
                                BottomAppBar(
                                    actions = {
                                        IconButton(onClick = { scope.launch { sheetState.show() } }) {
                                            Icon(Icons.Filled.Menu, "")
                                        }
                                        // TODO: Actions
                                    },
                                    floatingActionButton = {
                                        FloatingActionButton(
                                            onClick = { navigator.push(EntryFormTarget(null)) },
                                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                                        ) {
                                            Icon(Icons.Filled.Add, stringResource(MR.strings.entry_new))
                                        }
                                    },
                                )
                            },
                        )
                        if (sheetState.isVisible) {
                            BottomSheetNavigation(sheetState = sheetState)
                        }
                    }
                }
            }
        }
    }
}