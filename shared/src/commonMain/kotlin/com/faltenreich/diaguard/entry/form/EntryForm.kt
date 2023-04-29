package com.faltenreich.diaguard.entry.form

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.di.inject
import org.koin.core.parameter.parametersOf

@Composable
fun EntryForm(
    entry: Entry?,
    modifier: Modifier = Modifier,
    viewModel: EntryFormViewModel = inject { parametersOf(entry) },
) {
    val viewState = viewModel.viewState.collectAsState().value
    Column(modifier = modifier) {
        TextField(
            value = viewState.entry.note ?: "",
            onValueChange = viewModel::setNote,
        )
        Button(viewModel::submit) {
            Text("Submit")
        }
    }
}