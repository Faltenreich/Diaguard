package com.faltenreich.diaguard.tag.list

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import org.jetbrains.compose.resources.painterResource

data object TagListScreen : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.tags))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            floatingActionButton = {
                val viewModel = getViewModel<TagListViewModel>()
                FloatingActionButton(onClick = { viewModel.dispatchIntent(TagListIntent.CreateTag) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.tag_new),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        TagList(viewModel = getViewModel())
    }
}