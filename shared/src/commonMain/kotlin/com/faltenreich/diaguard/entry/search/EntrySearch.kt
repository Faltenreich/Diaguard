package com.faltenreich.diaguard.entry.search

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.view.SearchField
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun EntrySearch(query: String? = null) {
    SearchField(
        query = query,
        placeholder = stringResource(MR.strings.search_placeholder),
        onValueChange = {},
    )
}