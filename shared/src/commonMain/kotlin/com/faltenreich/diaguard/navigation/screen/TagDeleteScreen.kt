package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.delete.TagDeleteDialog
import org.koin.core.parameter.parametersOf

data class TagDeleteScreen(private val tag: Tag) : Screen {

    @Composable
    override fun Content() {
        TagDeleteDialog(viewModel = getViewModel { parametersOf(tag) })
    }
}