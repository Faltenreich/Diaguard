package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.tag.list.TagList

data object TagListScreen : Screen() {

    @Composable
    override fun Content() {
        TagList(viewModel = getViewModel())
    }
}