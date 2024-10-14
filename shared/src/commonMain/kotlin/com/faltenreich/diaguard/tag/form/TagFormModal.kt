package com.faltenreich.diaguard.tag.form

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.modal.Modal

data object TagFormModal : Modal {

    @Composable
    override fun Content() {
        TagFormDialog()
    }
}