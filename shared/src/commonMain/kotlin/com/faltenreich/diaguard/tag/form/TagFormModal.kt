package com.faltenreich.diaguard.tag.form

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.Modal

data object TagFormModal : Modal {

    @Composable
    override fun Content() {
        TagFormDialog()
    }
}