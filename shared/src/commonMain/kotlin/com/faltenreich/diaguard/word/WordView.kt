package com.faltenreich.diaguard.word

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.shared.di.inject
import org.koin.core.parameter.parametersOf

@Composable
fun WordView(
    word: Word,
    viewModel: WordViewModel = inject { parametersOf(word) },
) {
    Row(modifier = Modifier
        .clickable { viewModel.onClick() }
        .padding(all = 24.dp)
        .fillMaxWidth()
    ) {
        Text(word.name)
    }
}