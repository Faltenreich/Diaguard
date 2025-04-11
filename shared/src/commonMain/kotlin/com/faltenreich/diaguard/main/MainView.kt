package com.faltenreich.diaguard.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.faltenreich.diaguard.backup.user.read.ReadBackupFormScreen
import com.faltenreich.diaguard.backup.user.write.WriteBackupFormScreen
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.export.form.ExportFormScreen
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.food.form.FoodFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListScreen
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListScreen
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.navigation.navigate
import com.faltenreich.diaguard.navigation.screen
import com.faltenreich.diaguard.preference.color.ColorSchemeFormScreen
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesFormScreen
import com.faltenreich.diaguard.preference.food.FoodPreferenceListScreen
import com.faltenreich.diaguard.preference.license.LicenseListScreen
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceScreen
import com.faltenreich.diaguard.preference.screen.StartScreenFormScreen
import com.faltenreich.diaguard.shared.di.LocalSharedViewModelStoreOwner
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.di.rememberViewModelStoreOwner
import com.faltenreich.diaguard.statistic.StatisticScreen
import com.faltenreich.diaguard.tag.detail.TagDetailScreen
import com.faltenreich.diaguard.tag.list.TagListScreen
import com.faltenreich.diaguard.timeline.TimelineScreen

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = inject(),
) {
    val state = viewModel.collectState()
    if (state !is MainState.SubsequentStart) return

    val navController = rememberNavController()
    // Avoid recomposing Scaffold on changing preference
    val startScreen = remember { state.startScreen }

    LaunchedEffect(Unit) {
        viewModel.collectNavigationEvents { event ->
            when (event) {
                is NavigationEvent.PushScreen -> navController.navigate(
                    screen = event.screen,
                    popHistory = event.popHistory,
                )
                is NavigationEvent.PopScreen -> navController.popBackStack()
                is NavigationEvent.OpenBottomSheet -> TODO("Remove")
                is NavigationEvent.CloseBottomSheet -> TODO("Remove")
                is NavigationEvent.OpenModal -> TODO("Remove")
                is NavigationEvent.CloseModal -> TODO("Remove")
                is NavigationEvent.ShowSnackbar -> TODO("Remove")
            }
        }
    }

    val viewModelStoreOwner = rememberViewModelStoreOwner()

    CompositionLocalProvider(LocalSharedViewModelStoreOwner provides viewModelStoreOwner) {
        NavHost(
            navController = navController,
            startDestination = startScreen,
            modifier = modifier,
        ) {
            screen<DashboardScreen>()
            screen<TimelineScreen>()
            screen<LogScreen>()

            screen<EntryFormScreen>()
            screen<EntrySearchScreen>()
            screen<FoodFormScreen>()
            screen<FoodSearchScreen>()
            screen<FoodEatenListScreen>()
            screen<FoodPreferenceListScreen>()

            screen<StatisticScreen>()
            screen<ExportFormScreen>()

            screen<OverviewPreferenceScreen>()
            screen<ColorSchemeFormScreen>()
            screen<StartScreenFormScreen>()
            screen<DecimalPlacesFormScreen>()
            screen<WriteBackupFormScreen>()
            screen<ReadBackupFormScreen>()
            screen<MeasurementCategoryListScreen>()
            screen<MeasurementCategoryFormScreen>()
            screen<MeasurementPropertyFormScreen>()
            screen<MeasurementUnitListScreen>()
            screen<TagListScreen>()
            screen<TagDetailScreen>()
            screen<LicenseListScreen>()
        }
    }
}