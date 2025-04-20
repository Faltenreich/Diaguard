package com.faltenreich.diaguard.preference.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceListItemScaffold

@Composable
fun PreferenceList(
    items: List<PreferenceListItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items) { preference ->
            preference.Content(Modifier)
        }
    }
}

fun LazyListScope.action(
    title: @Composable () -> String,
    onClick: () -> Unit,
    subtitle: @Composable (() -> String)? = null,
    modifier: Modifier = Modifier,
) {
    item {
        PreferenceListItemScaffold(
            title = title() ,
            subtitle = subtitle?.invoke(),
            modifier = modifier.clickable { onClick() },
        )
    }
}