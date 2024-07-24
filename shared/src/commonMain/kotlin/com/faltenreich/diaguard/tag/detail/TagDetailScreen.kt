package com.faltenreich.diaguard.tag.detail

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.tag.Tag
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.save
import diaguard.shared.generated.resources.tag
import diaguard.shared.generated.resources.tag_delete
import org.jetbrains.compose.resources.painterResource

data class TagDetailScreen(private val tag: Tag.Local) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.tag))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<TagDetailViewModel> { TagDetailViewModel(tag) }
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.tag_delete,
                    onClick = { viewModel.dispatchIntent(TagDetailIntent.DeleteTag) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<TagDetailViewModel> { TagDetailViewModel(tag) }
                FloatingActionButton(onClick = { viewModel.dispatchIntent(TagDetailIntent.UpdateTag) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.save),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        TagDetail(viewModel = getViewModel { TagDetailViewModel(tag) })
    }
}