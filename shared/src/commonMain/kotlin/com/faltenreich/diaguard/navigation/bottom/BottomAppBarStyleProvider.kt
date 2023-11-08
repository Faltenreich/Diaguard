package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.export.ExportFormViewModel
import com.faltenreich.diaguard.food.list.FoodListViewModel
import com.faltenreich.diaguard.log.LogViewModel
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormViewModel
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyListViewModel
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormViewModel
import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.navigation.screen.ExportFormScreen
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyFormScreen
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyListScreen
import com.faltenreich.diaguard.navigation.screen.MeasurementTypeFormScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DatePickerBottomAppBarItem
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.shared.view.SearchField
import com.faltenreich.diaguard.timeline.TimelineViewModel
import org.koin.core.parameter.parametersOf

fun Screen.bottomAppBarStyle(): BottomAppBarStyle {
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
                val viewState = viewModel.viewState.collectAsState().value
                EntrySearchBottomAppBarItem()
                DatePickerBottomAppBarItem(
                    date = viewState.currentDate,
                    onDatePick = viewModel::setDate,
                )
            },
            floatingActionButton = { EntryFormFloatingActionButton() },
        )
        is EntryFormScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<EntryFormViewModel> { parametersOf(entry, date) }
                val navigator = LocalNavigator.currentOrThrow
                BottomAppBarItem(
                    image = Icons.Filled.Delete,
                    contentDescription = MR.strings.entry_delete,
                    onClick = { viewModel.deleteIfNeeded(); navigator.pop() },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<EntryFormViewModel> { parametersOf(entry, date) }
                val navigator = LocalNavigator.currentOrThrow
                FloatingActionButton(onClick = { viewModel.submit(); navigator.pop() }) {
                    Icon(Icons.Filled.Check, getString(MR.strings.entry_save))
                }
            }
        )
        is EntrySearchScreen -> BottomAppBarStyle.Visible(
            actions = {
                val navigator = LocalNavigator.currentOrThrow
                val viewModel = getViewModel<EntrySearchViewModel> { parametersOf(query) }
                SearchField(
                    query = viewModel.query,
                    placeholder = getString(MR.strings.entry_search_prompt),
                    onQueryChange = { query ->
                        if (query.isBlank()) navigator.pop()
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
                FloatingActionButton(onClick = { TODO() }) {
                    Icon(Icons.Filled.Add, getString(MR.strings.food_new))
                }
            }
        )
        is ExportFormScreen -> BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<ExportFormViewModel>()
                FloatingActionButton(onClick = { viewModel.submit() }) {
                    Icon(Icons.Filled.Check, getString(MR.strings.export))
                }
            }
        )
        is MeasurementPropertyListScreen -> BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementPropertyListViewModel>()
                FloatingActionButton(onClick = viewModel::showFormDialog) {
                    Icon(Icons.Filled.Add, getString(MR.strings.measurement_property_new))
                }
            }
        )
        is MeasurementPropertyFormScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<MeasurementPropertyFormViewModel> { parametersOf(property) }
                BottomAppBarItem(
                    image = Icons.Filled.Delete,
                    contentDescription = MR.strings.measurement_property_delete,
                    onClick = viewModel::deletePropertyIfConfirmed,
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<MeasurementPropertyFormViewModel> { parametersOf(property) }
                FloatingActionButton(onClick = viewModel::showFormDialog) {
                    Icon(Icons.Filled.Add, getString(MR.strings.measurement_type_new))
                }
            }
        )
        is MeasurementTypeFormScreen -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<MeasurementTypeFormViewModel> { parametersOf(measurementTypeId) }
                BottomAppBarItem(
                    image = Icons.Filled.Delete,
                    contentDescription = MR.strings.measurement_type_delete,
                    onClick = viewModel::deleteTypeIfConfirmed,
                )
            },
        )
        else -> BottomAppBarStyle.Visible()
    }
}