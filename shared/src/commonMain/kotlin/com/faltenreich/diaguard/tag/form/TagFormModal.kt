package com.faltenreich.diaguard.tag.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.shared.di.viewModel

data object TagFormModal : Modal {

    @Composable
    override fun Content(modifier: Modifier) {
        TagFormDialog(viewModel = viewModel())
    }
}