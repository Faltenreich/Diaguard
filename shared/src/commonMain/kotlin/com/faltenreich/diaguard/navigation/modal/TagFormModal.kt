package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.tag.form.TagFormDialog

data object TagFormModal : Modal {

    @Composable
    override fun Content() {
        TagFormDialog()
    }
}