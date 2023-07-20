package com.faltenreich.diaguard.navigation

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.entry.search.EntrySearch
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.preference.PreferenceList
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.timeline.Timeline
import org.koin.core.parameter.parametersOf
import cafe.adriel.voyager.core.screen.Screen as VoyagerScreen

sealed class Screen : VoyagerScreen {

    object Dashboard : Screen() {

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

    object PreferenceList : Screen() {

        @Composable
        override fun Content() {
            PreferenceList(viewModel = getViewModel())
        }
    }
}