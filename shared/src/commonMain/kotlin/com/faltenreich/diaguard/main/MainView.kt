package com.faltenreich.diaguard.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.export.ExportFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationIntent
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBar
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.bottomsheet.BottomSheetNavigationScreen
import com.faltenreich.diaguard.navigation.screen
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesFormScreen
import com.faltenreich.diaguard.preference.list.PreferenceListScreen
import com.faltenreich.diaguard.shared.di.LocalSharedViewModelStoreOwner
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.di.rememberViewModelStoreOwner
import com.faltenreich.diaguard.timeline.TimelineScreen

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = inject(),
    navigation: Navigation = inject(),
) {
    val state = viewModel.collectState()
    if (state !is MainState.SubsequentStart) return

    val currentScreen = state.currentScreen
    val bottomSheet = state.bottomSheet
    val modal = state.modal

    val navController = rememberNavController()
    SideEffect { navigation.navController = navController }

    val snackbarHostState = remember { SnackbarHostState() }
    SideEffect { navigation.snackbarState = snackbarHostState }

    val viewModelStoreOwner = rememberViewModelStoreOwner()

    CompositionLocalProvider(LocalSharedViewModelStoreOwner provides viewModelStoreOwner) {
        Scaffold(
            modifier = modifier,
            topBar = { TopAppBar(currentScreen?.topAppBarStyle ?: TopAppBarStyle.Hidden) },
            content = { padding ->

                NavHost(
                    navController = navController,
                    startDestination = DashboardScreen,
                    modifier = Modifier.padding(padding),
                ) {
                    screen<DashboardScreen>()
                    screen<TimelineScreen>()
                    screen<LogScreen>()

                    screen<EntryFormScreen>()
                    screen<EntrySearchScreen>()
                    screen<FoodSearchScreen>()
                    screen<ExportFormScreen>()

                    screen<PreferenceListScreen>()
                    screen<DecimalPlacesFormScreen>()
                    screen<MeasurementCategoryListScreen>()
                }

                if (bottomSheet != null) {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.dispatchIntent(NavigationIntent.CloseBottomSheet) },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    ) {
                        bottomSheet.Content()
                    }
                }

                modal?.Content()
            },
            bottomBar = {
                BottomAppBar(
                    style = currentScreen?.bottomAppBarStyle ?: BottomAppBarStyle.Hidden,
                    onMenuClick = {
                        viewModel.dispatchIntent(
                            NavigationIntent.OpenBottomSheet(
                                BottomSheetNavigationScreen
                            )
                        )
                    },
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        )
    }
}