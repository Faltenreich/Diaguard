package com.faltenreich.diaguard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
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
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryForm
import com.faltenreich.diaguard.navigation.NavigationViewModel
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.datetime.DateTimeRepository
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun MainView() {
    MainTheme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = { BottomNavigation() },
            ) { padding ->
                Content(modifier = Modifier.padding(padding))
            }
        }
    }
}

@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel = inject(),
    dateTimeRepository: DateTimeRepository = inject(),
) {
    BottomAppBar(
        modifier = modifier,
        actions = {
            IconButton(onClick = { viewModel.navigate(Screen.Dashboard) }) {
                Icon(Icons.Filled.List, "")
            }
        },
        floatingActionButton = { ActionButton { viewModel.navigate(Screen.EntryForm(Entry(0, dateTimeRepository.now(), null))) } },
    )
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
    ) {
        Icon(Icons.Filled.Add, "")
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel = inject(),
) {
    when (viewModel.uiState.collectAsState().value) {
        is Screen.Dashboard -> Dashboard(modifier = modifier)
        is Screen.EntryForm -> EntryForm(modifier = modifier)
    }
}