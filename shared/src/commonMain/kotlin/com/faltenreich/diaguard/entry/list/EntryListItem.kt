package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.shared.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.ClearButton

@Composable
fun EntryListItem(
    entry: Entry,
    modifier: Modifier = Modifier,
    onDelete: (Entry) -> Unit,
) {
    val navigator = LocalNavigator.currentOrThrow
    val formatter = inject<DateTimeFormatter>()
    Row(
        modifier = modifier
            .clickable(onClick = { navigator.push(EntryForm(entry)) })
            .padding(AppTheme.dimensions.padding.P_3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = entry.id.toString(),
            modifier = Modifier.width(AppTheme.dimensions.size.MinimumTouchSize),
        )
        Column(modifier = Modifier.weight(AppTheme.dimensions.weight.W_1)) {
            Text(formatter.format(entry.dateTime))
            Text(entry.note ?: "")
        }
        ClearButton { onDelete(entry) }
    }
}