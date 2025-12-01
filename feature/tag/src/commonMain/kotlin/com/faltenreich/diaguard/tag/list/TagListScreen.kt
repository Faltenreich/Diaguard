package com.faltenreich.diaguard.tag.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.ic_add
import com.faltenreich.diaguard.resource.tag_new
import com.faltenreich.diaguard.resource.tags
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Serializable
data object TagListScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.tags))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<TagListViewModel>()
        return BottomAppBarStyle.Visible(
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.tag_new),
                    onClick = { viewModel.dispatchIntent(TagListIntent.OpenFormDialog) },
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<TagListViewModel>()
        TagList(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}