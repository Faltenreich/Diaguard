package com.faltenreich.diaguard.navigation

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.entry.search.EntrySearch
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyForm
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyList
import com.faltenreich.diaguard.preference.list.Preference
import com.faltenreich.diaguard.preference.list.PreferenceList
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.timeline.Timeline
import org.koin.core.parameter.parametersOf
import cafe.adriel.voyager.core.screen.Screen as VoyagerScreen

/**
 * Component that can be navigated to
 *
 * State restoration requires every parameter to implement
 * [com.faltenreich.diaguard.shared.serialization.Serializable]
 */
sealed class Screen : VoyagerScreen {

    data object Dashboard : Screen() {

        @Composable
        override fun Content() {
            Dashboard(viewModel = getViewModel())
        }
    }

    data class Log(val date: Date = Date.today()) : Screen() {

        @Composable
        override fun Content() {
            Log(viewModel = getViewModel { parametersOf(date) })
        }
    }

    data class Timeline(val date: Date = Date.today()) : Screen() {

        @Composable
        override fun Content() {
            Timeline(viewModel = getViewModel { parametersOf(date) })
        }
    }

    data class EntryForm(val entry: Entry? = null, val date: Date? = null) : Screen() {

        @Composable
        override fun Content() {
            EntryForm(viewModel = getViewModel { parametersOf(entry, date) })
        }
    }

    data class EntrySearch(val query: String? = null) : Screen() {

        @Composable
        override fun Content() {
            EntrySearch(viewModel = getViewModel { parametersOf(query) })
        }
    }

    data object MeasurementPropertyList : Screen() {

        @Composable
        override fun Content() {
            MeasurementPropertyList(viewModel = getViewModel())
        }
    }

    data class MeasurementPropertyForm(val property: MeasurementProperty) : Screen() {

        @Composable
        override fun Content() {
            MeasurementPropertyForm(viewModel = getViewModel { parametersOf(property) })
        }
    }

    // FIXME: Supports no state restoration until parameter implements Serializable
    data class PreferenceList(val preferences: List<Preference>? = null) : Screen() {

        @Composable
        override fun Content() {
            // FIXME: Reuses same instance for different parameters
            val viewModel = getViewModel<PreferenceListViewModel> { parametersOf(preferences) }
            PreferenceList(viewModel = viewModel)
        }
    }
}