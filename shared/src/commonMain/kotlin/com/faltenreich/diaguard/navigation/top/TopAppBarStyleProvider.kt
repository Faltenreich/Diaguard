package com.faltenreich.diaguard.navigation.top

import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.log.LogViewModel
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.getViewModel
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.parameter.parametersOf

fun Screen.topAppBarStyle(): TopAppBarStyle {
    return when (this) {
        is Screen.EntryForm -> TopAppBarStyle.CenterAligned {
            val viewModel = getViewModel<EntryFormViewModel> { parametersOf(entry) }
            val viewState = viewModel.viewState.collectAsState().value
            val title = if (viewState.isEditing) MR.strings.entry_edit else MR.strings.entry_new
            Text(stringResource(title))
        }
        is Screen.Log -> TopAppBarStyle.CenterAligned {
            val viewModel = getViewModel<LogViewModel> { parametersOf(date) }
            val viewState = viewModel.viewState.collectAsState().value
            val title = viewState.date.toString()
            Text(title)
        }
        else -> TopAppBarStyle.Hidden
    }
}