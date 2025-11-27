package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.backup.read.ReadBackupFormScreen
import com.faltenreich.diaguard.backup.write.WriteBackupFormScreen
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.export.form.ExportFormScreen
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.food.form.FoodFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormScreen
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListScreen
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListMode
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.preference.food.FoodPreferenceListScreen
import com.faltenreich.diaguard.preference.license.LicenseListScreen
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceListScreen
import com.faltenreich.diaguard.statistic.StatisticScreen
import com.faltenreich.diaguard.tag.detail.TagDetailScreen
import com.faltenreich.diaguard.tag.list.TagListScreen
import com.faltenreich.diaguard.timeline.TimelineScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNavigationEventUseCase(
    private val navigation: Navigation,
) {

    operator fun invoke(): Flow<MainEvent>{
        return navigation.events.map { event ->
            with (event) {
                when (this) {
                    is NavigationEvent.NavigateTo -> MainEvent.NavigateTo(
                        screen = target.toScreen(),
                        clearHistory = clearHistory,
                    )
                    is NavigationEvent.NavigateBack -> MainEvent.NavigateBack
                    is NavigationEvent.ShowSnackbar -> MainEvent.ShowSnackbar(
                        message = message,
                        actionLabel = actionLabel,
                        withDismissAction = withDismissAction,
                        duration = duration,
                    )
                }
            }
        }
    }

    private fun NavigationTarget.toScreen(): Screen = when (this) {
        is NavigationTarget.Dashboard -> DashboardScreen
        is NavigationTarget.EntryForm ->
            EntryFormScreen(entryId = entryId, dateTimeIsoString = dateTime?.isoString, foodId = foodId)
        is NavigationTarget.EntrySearch -> EntrySearchScreen(query)
        is NavigationTarget.ExportForm -> ExportFormScreen
        is NavigationTarget.FoodEatenList -> FoodEatenListScreen(foodId = foodId)
        is NavigationTarget.FoodForm -> FoodFormScreen(foodId = foodId)
        is NavigationTarget.FoodPreferenceList -> FoodPreferenceListScreen
        is NavigationTarget.FoodSearch -> FoodSearchScreen(mode = mode.toDomain())
        is NavigationTarget.LicenseList -> LicenseListScreen
        is NavigationTarget.Log -> LogScreen
        is NavigationTarget.MeasurementCategoryForm -> MeasurementCategoryFormScreen(categoryId = categoryId)
        is NavigationTarget.MeasurementCategoryList -> MeasurementCategoryListScreen
        is NavigationTarget.MeasurementPropertyForm ->
            MeasurementPropertyFormScreen(categoryId = categoryId, propertyId = propertyId)
        is NavigationTarget.MeasurementUnitList -> MeasurementUnitListScreen(mode = mode.toDomain())
        is NavigationTarget.OverviewPreferenceList -> OverviewPreferenceListScreen
        is NavigationTarget.ReadBackupForm -> ReadBackupFormScreen
        is NavigationTarget.Statistic -> StatisticScreen
        is NavigationTarget.TagDetail -> TagDetailScreen(tagId = tagId)
        is NavigationTarget.TagList -> TagListScreen
        is NavigationTarget.Timeline -> TimelineScreen
        is NavigationTarget.WriteBackupForm -> WriteBackupFormScreen
    }

    private fun NavigationTarget.FoodSearch.Mode.toDomain(): FoodSearchMode = when (this) {
        NavigationTarget.FoodSearch.Mode.STROLL -> FoodSearchMode.STROLL
        NavigationTarget.FoodSearch.Mode.FIND -> FoodSearchMode.FIND
    }

    private fun NavigationTarget.MeasurementUnitList.Mode.toDomain(): MeasurementUnitListMode = when (this) {
        NavigationTarget.MeasurementUnitList.Mode.STROLL -> MeasurementUnitListMode.STROLL
        NavigationTarget.MeasurementUnitList.Mode.FIND -> MeasurementUnitListMode.FIND
    }
}