package com.faltenreich.diaguard.navigation.top

import androidx.compose.material3.Text
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.getViewModel
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.parameter.parametersOf

fun Screen.topAppBarStyle(): TopAppBarStyle {
    return when (this) {
        is Screen.EntryForm -> TopAppBarStyle.CenterAligned {
            val viewModel = getViewModel<EntryFormViewModel> { parametersOf(entry, date) }
            val titleResource = if (viewModel.isEditing) MR.strings.entry_edit else MR.strings.entry_new
            Text(stringResource(titleResource))
        }
        is Screen.PreferenceList -> TopAppBarStyle.CenterAligned {
            Text(stringResource(MR.strings.preferences))
        }
        else -> TopAppBarStyle.Hidden
    }
}