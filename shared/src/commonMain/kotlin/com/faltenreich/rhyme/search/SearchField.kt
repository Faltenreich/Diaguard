package com.faltenreich.rhyme.search

import LanguageButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.rhyme.MR
import com.faltenreich.rhyme.shared.di.inject
import com.faltenreich.rhyme.shared.localization.Localization
import com.faltenreich.rhyme.shared.view.ClearButton

@Composable
fun SearchField(
    query: String,
    onValueChange: (String) -> Unit,
    localization: Localization = inject(),
) {
    TextField(
        query,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(localization.getString(MR.strings.search_placeholder)) },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ClearButton(onClick = { onValueChange("") })
                LanguageButton()
            }
        },
        shape = RoundedCornerShape(40),
    )
}