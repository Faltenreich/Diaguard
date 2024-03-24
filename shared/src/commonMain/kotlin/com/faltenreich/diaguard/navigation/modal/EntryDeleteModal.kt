package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.delete.EntryDeleteDialog
import com.faltenreich.diaguard.shared.di.inject
import org.koin.core.parameter.parametersOf

data class EntryDeleteModal(private val entry: Entry?) : Modal {

    @Composable
    override fun Content() {
        EntryDeleteDialog(viewModel = inject { parametersOf(entry) })
    }
}