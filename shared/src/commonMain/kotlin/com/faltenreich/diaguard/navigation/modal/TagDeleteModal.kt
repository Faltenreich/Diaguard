package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.delete.TagDeleteDialog
import org.koin.core.parameter.parametersOf

// TODO: Replace with DeleteModal
data class TagDeleteModal(private val tag: Tag) : Modal {

    @Composable
    override fun Content() {
        TagDeleteDialog(viewModel = inject { parametersOf(tag) })
    }
}