package com.faltenreich.diaguard.log.list.item

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogMonth(
    state: LogItemState.MonthHeader,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = { Text(state.dateLocalized) },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colors.scheme.primary,
            navigationIconContentColor = AppTheme.colors.scheme.onPrimary,
            titleContentColor = AppTheme.colors.scheme.onPrimary,
            actionIconContentColor = AppTheme.colors.scheme.onPrimary,
        ),
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    val date = today()
    LogMonth(
        state = LogItemState.MonthHeader(
            date = date,
            dateLocalized = date.toString(),
        ),
    )
}