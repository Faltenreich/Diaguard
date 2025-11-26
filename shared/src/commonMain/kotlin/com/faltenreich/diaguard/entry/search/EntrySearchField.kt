package com.faltenreich.diaguard.entry.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.view.button.ClearButton
import com.faltenreich.diaguard.view.input.SearchField
import com.faltenreich.diaguard.view.layout.rememberFocusRequester
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_search_prompt
import diaguard.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntrySearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = rememberFocusRequester(requestFocus = true)
    val focusManager = LocalFocusManager.current

    SearchField(
        query = query,
        placeholder = stringResource(Res.string.entry_search_prompt),
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = null,
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                ClearButton(
                    onClick = {
                        onQueryChange("")
                        focusManager.clearFocus()
                    },
                )
            }
        },
        onQueryChange = { query -> onQueryChange(query) },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
    )
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    EntrySearchField(
        query = "",
        onQueryChange = {},
    )
}