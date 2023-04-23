package com.faltenreich.diaguard.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun EntryForm(
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel = inject(),
) {
    val viewState = viewModel.viewState.collectAsState()
    val entry = viewState.value.entry
    Column(modifier = modifier) {
        TextField(
            value = entry.note ?: "",
            onValueChange = viewModel::setNote,
        )
        Button(viewModel::submit) {
            Text("Submit")
        }
    }
}