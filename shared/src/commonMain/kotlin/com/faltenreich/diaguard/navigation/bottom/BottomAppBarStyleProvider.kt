package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.export.ExportFormViewModel
import com.faltenreich.diaguard.food.form.FoodFormIntent
import com.faltenreich.diaguard.food.form.FoodFormViewModel
import com.faltenreich.diaguard.food.list.FoodListViewModel
import com.faltenreich.diaguard.log.LogViewModel
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormIntent
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormViewModel
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyListIntent
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyListViewModel
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormIntent
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormViewModel
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.navigation.screen.ExportFormScreen
import com.faltenreich.diaguard.navigation.screen.FoodEatenListScreen
import com.faltenreich.diaguard.navigation.screen.FoodFormScreen
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyListScreen
import com.faltenreich.diaguard.navigation.screen.MeasurementTypeFormScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.TagListScreen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DatePickerBottomAppBarItem
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.shared.view.SearchField
import com.faltenreich.diaguard.tag.list.TagListIntent
import com.faltenreich.diaguard.tag.list.TagListViewModel
import com.faltenreich.diaguard.timeline.TimelineIntent
import com.faltenreich.diaguard.timeline.TimelineViewModel
import dev.icerock.moko.resources.compose.painterResource
import org.koin.core.parameter.parametersOf

fun Screen.bottomAppBarStyle(
    navigation: Navigation = inject(),
): BottomAppBarStyle {
    return when (this) {
        is DashboardScreen -> BottomAppBarStyle.Visible(
            actions = { EntrySearchBottomAppBarItem() },
            floatingActionButton = { EntryFormFloatingActionButton() },
        )
        is LogScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<LogViewModel> { parametersOf(date) }
                val currentDate = viewModel.currentDate.collectAsState().value
                EntrySearchBottomAppBarItem()
                DatePickerBottomAppBarItem(
                    date = currentDate,
                    onDatePick = viewModel::setDate,
                )
            },
            floatingActionButton = { EntryFormFloatingActionButton() },
        )
        is TimelineScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<TimelineViewModel> { parametersOf(date) }
                val viewState = viewModel.collectState()
                EntrySearchBottomAppBarItem()
                DatePickerBottomAppBarItem(
                    date = viewState?.currentDate,
                    onDatePick = { viewModel.dispatchIntent(TimelineIntent.SetDate(it)) },
                )
            },
            floatingActionButton = { EntryFormFloatingActionButton() },
        )
        is EntryFormScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<EntryFormViewModel> { parametersOf(entry, date) }
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_delete),
                    contentDescription = MR.strings.entry_delete,
                    onClick = { viewModel.dispatchIntent(EntryFormIntent.Delete); navigation.pop() },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<EntryFormViewModel> { parametersOf(entry, date) }
                FloatingActionButton(onClick = { viewModel.dispatchIntent(EntryFormIntent.Submit); navigation.pop() }) {
                    Icon(
                        painter = painterResource(MR.images.ic_check),
                        contentDescription = getString(MR.strings.entry_save),
                    )
                }
            }
        )
        is EntrySearchScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<EntrySearchViewModel> { parametersOf(query) }
                SearchField(
                    query = viewModel.query,
                    placeholder = getString(MR.strings.entry_search_prompt),
                    onQueryChange = { query ->
                        if (query.isBlank()) navigation.pop()
                        else viewModel.query = query
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = AppTheme.dimensions.padding.P_3),
                )
            }
        )
        is FoodListScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<FoodListViewModel>()
                SearchField(
                    query = viewModel.query,
                    placeholder = getString(MR.strings.food_search_prompt),
                    onQueryChange = { viewModel.query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = AppTheme.dimensions.padding.P_3),
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navigation.push(FoodFormScreen()) }) {
                    Icon(
                        painter = painterResource(MR.images.ic_add),
                        contentDescription = getString(MR.strings.food_new),
                    )
                }
            }
        )
        is FoodEatenListScreen -> BottomAppBarStyle.Visible(
            floatingActionButton = {
                FloatingActionButton(onClick = { navigation.push(EntryFormScreen(food = food)) }) {
                    Icon(
                        painter = painterResource(MR.images.ic_add),
                        contentDescription = getString(MR.strings.entry_new_description),
                    )
                }
            },
        )
        is FoodFormScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<FoodFormViewModel> { parametersOf(food) }
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_delete),
                    contentDescription = MR.strings.food_delete,
                    onClick = { viewModel.dispatchIntent(FoodFormIntent.Delete); navigation.pop() },
                )
                food?.let {
                    BottomAppBarItem(
                        painter = painterResource(MR.images.ic_history),
                        contentDescription = MR.strings.food_eaten,
                        onClick = { navigation.push(FoodEatenListScreen(food)) },
                    )
                }
            },
            floatingActionButton = {
                val viewModel = getViewModel<FoodFormViewModel> { parametersOf(food) }
                FloatingActionButton(onClick = { viewModel.dispatchIntent(FoodFormIntent.Submit); navigation.pop() }) {
                    Icon(
                        painter = painterResource(MR.images.ic_check),
                        contentDescription = getString(MR.strings.food_save),
                    )
                }
            }
        )
        is TagListScreen -> BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<TagListViewModel>()
                FloatingActionButton(onClick = { viewModel.dispatchIntent(TagListIntent.OpenForm) }) {
                    Icon(
                        painter = painterResource(MR.images.ic_add),
                        contentDescription = getString(MR.strings.tag_new),
                    )
                }
            }
        )
        is ExportFormScreen -> BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<ExportFormViewModel>()
                FloatingActionButton(onClick = { viewModel.submit() }) {
                    Icon(
                        painter = painterResource(MR.images.ic_check),
                        contentDescription = getString(MR.strings.export),
                    )
                }
            }
        )
        is MeasurementPropertyListScreen -> BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementPropertyListViewModel>()
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyListIntent.ShowFormDialog) },
                ) {
                    Icon(
                        painter = painterResource(MR.images.ic_add),
                        contentDescription = getString(MR.strings.measurement_property_new),
                    )
                }
            }
        )
        is MeasurementPropertyFormScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<MeasurementPropertyFormViewModel> { parametersOf(property) }
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_delete),
                    contentDescription = MR.strings.measurement_property_delete,
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.ShowDeletionDialog) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementPropertyFormViewModel> { parametersOf(property) }
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(MeasurementPropertyFormIntent.ShowFormDialog) },
                ) {
                    Icon(
                        painter = painterResource(MR.images.ic_add),
                        contentDescription = getString(MR.strings.measurement_type_new),
                    )
                }
            }
        )
        is MeasurementTypeFormScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<MeasurementTypeFormViewModel> { parametersOf(measurementTypeId) }
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_delete),
                    contentDescription = MR.strings.measurement_type_delete,
                    onClick = { viewModel.dispatchIntent(MeasurementTypeFormIntent.ShowDeletionDialog) },
                )
            },
        )
        else -> BottomAppBarStyle.Visible()
    }
}