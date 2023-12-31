package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.tag.form.TagFormDialog

data class TagFormScreen(
    val onDismissRequest: () -> Unit,
    val onConfirmRequest: (name: String) -> Unit,
    val error: String?,
) : Screen {

    @Composable
    override fun Content() {
        TagFormDialog(onDismissRequest, onConfirmRequest, error)
    }
}