package com.faltenreich.diaguard.word

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization

@Composable
fun WordListView(
    words: Map<RhymeType, List<Word>>,
    localization: Localization = inject(),
) {
    LazyColumn {
        words.forEach { (rhymeType, words) ->
            stickyHeader {
                Text(
                    localization.getString(rhymeType.textResource),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            items(words) { word ->
                WordView(word)
                Divider()
            }
        }
    }
}