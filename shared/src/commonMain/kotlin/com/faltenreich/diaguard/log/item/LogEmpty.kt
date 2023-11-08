package com.faltenreich.diaguard.log.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun LogEmpty(
    date: Date,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    Text(
        text = getString(MR.strings.no_entries),
        modifier = modifier
            .clickable { navigator.push(EntryFormScreen(date = date)) }
            .fillMaxWidth()
            .height(AppTheme.dimensions.size.TouchSizeMedium)
            .padding(all = AppTheme.dimensions.padding.P_3),
    )
}