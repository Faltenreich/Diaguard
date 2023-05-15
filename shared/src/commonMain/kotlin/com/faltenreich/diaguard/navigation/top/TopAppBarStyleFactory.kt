package com.faltenreich.diaguard.navigation.top

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.getViewModel
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.parameter.parametersOf

object TopAppBarStyleFactory {

    @Composable
    fun forScreen(screen: Screen): TopAppBarStyle {
        return when (screen) {
            is Screen.EntryForm -> TopAppBarStyle.CenterAligned {
                val viewModel = screen.getViewModel<EntryFormViewModel> { parametersOf(screen.entry) }
                val viewState = viewModel.viewState.collectAsState().value
                val title = if (viewState.isEditing) MR.strings.entry_edit else MR.strings.entry_new
                Text(stringResource(title))
            }
            else -> TopAppBarStyle.Hidden
        }
    }
}