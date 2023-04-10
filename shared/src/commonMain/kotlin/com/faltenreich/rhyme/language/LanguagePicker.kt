package com.faltenreich.rhyme.language

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.rhyme.shared.di.inject
import com.faltenreich.rhyme.shared.view.DropDownMenu
import com.faltenreich.rhyme.shared.view.DropDownMenuItem

@Composable
fun LanguagePicker(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    viewModel: LanguageViewModel = inject(),
) {
    val state = viewModel.uiState.collectAsState().value
    DropDownMenu(expanded, onDismissRequest) {
        state.languages.forEach { language ->
            val iSelected = state.currentLanguage == language
            val foregroundColor =
                if (iSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onBackground
            val backgroundColor =
                if (iSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            DropDownMenuItem(
                text = { Text(language.title, color = foregroundColor) },
                onClick = { onDismissRequest(); viewModel.setCurrentLanguage(language) },
                modifier = Modifier.background(backgroundColor)
            )
        }
    }
}