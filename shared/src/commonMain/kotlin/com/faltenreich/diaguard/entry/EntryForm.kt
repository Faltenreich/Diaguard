package com.faltenreich.diaguard.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun EntryForm(
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel = inject(),
) {
    Column {
        Text("Entry Form", modifier = modifier)
        Button(viewModel::createEntry) {
            Text("Create entry")
        }
        viewModel.getAll().forEach { entry ->
            Text("Entry ${entry.id}: ${entry.dateTime}")
        }
    }
}