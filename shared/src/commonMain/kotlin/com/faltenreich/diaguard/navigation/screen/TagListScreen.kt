package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.tag.list.TagList
import com.faltenreich.diaguard.tag.list.TagListIntent
import com.faltenreich.diaguard.tag.list.TagListViewModel
import dev.icerock.moko.resources.compose.painterResource

data object TagListScreen : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.tags))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<TagListViewModel>()
                FloatingActionButton(onClick = { viewModel.dispatchIntent(TagListIntent.CreateTag) }) {
                    Icon(
                        painter = painterResource(MR.images.ic_add),
                        contentDescription = getString(MR.strings.tag_new),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        TagList(viewModel = getViewModel())
    }
}