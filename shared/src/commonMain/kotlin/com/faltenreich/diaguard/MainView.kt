package com.faltenreich.diaguard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.navigation.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.NavigationViewModel
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.BottomSheetState
import com.faltenreich.diaguard.timeline.Timeline
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@Composable
fun MainView() {
    val scope = rememberCoroutineScope()
    val sheetState = remember { BottomSheetState() }

    MainTheme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box {
                Scaffold(
                    bottomBar = {
                        BottomNavigation(
                            onMenuButtonClick = { scope.launch { sheetState.show() } },
                        )
                    },
                ) { padding ->
                    Content(modifier = Modifier.padding(padding))
                }

                if (sheetState.isVisible) {
                    BottomSheetNavigation(sheetState = sheetState)
                }
            }
        }
    }
}

@Composable
private fun BottomNavigation(
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel = inject(),
    dateTimeApi: DateTimeApi = inject(),
) {
    BottomAppBar(
        modifier = modifier,
        actions = {
            IconButton(onClick = onMenuButtonClick) {
                Icon(Icons.Filled.Menu, "")
            }
            // TODO: Actions
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.navigate(Screen.EntryForm(Entry(0, dateTimeApi.now(), null))) },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            ) {
                Icon(Icons.Filled.Add, stringResource(MR.strings.entry_new))
            }
        },
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel = inject(),
) {
    when (viewModel.uiState.collectAsState().value) {
        is Screen.Dashboard -> Dashboard(modifier = modifier)
        is Screen.Timeline -> Timeline(modifier = modifier)
        is Screen.Log -> Log(modifier = modifier)
        is Screen.EntryForm -> EntryForm(modifier = modifier)
    }
}