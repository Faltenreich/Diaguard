package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.tag.form.TagFormDialog

data object TagFormScreen : Screen {

    @Composable
    override fun Content() {
        TagFormDialog()
    }
}