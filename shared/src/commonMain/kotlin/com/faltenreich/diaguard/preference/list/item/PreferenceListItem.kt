package com.faltenreich.diaguard.preference.list.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun PreferenceListItem(
    modifier: Modifier = Modifier,
    top: @Composable (() -> Unit)? = null,
    left: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        top?.invoke()
        Row(
            modifier = Modifier
                .padding(all = AppTheme.dimensions.padding.P_3)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.width(AppTheme.dimensions.size.ListOffsetWidth)) {
                left?.invoke()
            }
            content()
        }
    }
}