package com.faltenreich.diaguard.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.navigation.screen
import com.faltenreich.diaguard.preference.list.PreferenceListScreen
import com.faltenreich.diaguard.shared.di.LocalSharedViewModelStoreOwner
import com.faltenreich.diaguard.shared.di.rememberViewModelStoreOwner

@Composable
fun MainNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModelStoreOwner = rememberViewModelStoreOwner()
    // TODO: Too late for Top-/BottomAppBar
    CompositionLocalProvider(LocalSharedViewModelStoreOwner provides viewModelStoreOwner) {
        NavHost(
            navController = navController,
            startDestination = DashboardScreen,
            modifier = modifier,
        ) {
            screen<DashboardScreen>()
            screen<EntryFormScreen>()
            screen<EntrySearchScreen>()
            screen<FoodSearchScreen>()
            screen<PreferenceListScreen>()
        }
    }
}