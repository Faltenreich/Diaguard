package com.faltenreich.diaguard.food.eaten.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.entry_new_description
import com.faltenreich.diaguard.resource.food_eaten
import com.faltenreich.diaguard.resource.ic_add
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import com.faltenreich.diaguard.view.theme.AppTheme
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class FoodEatenListScreen(private val foodId: Long) : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        val viewModel = viewModel<FoodEatenListViewModel> { parametersOf(foodId) }
        return TopAppBarStyle.CenterAligned {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(Res.string.food_eaten))
                Text(
                    text = viewModel.food.name,
                    style = AppTheme.typography.bodySmall,
                )
            }
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<FoodEatenListViewModel> { parametersOf(foodId) }
        return BottomAppBarStyle.Visible(
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.entry_new_description),
                    onClick = { viewModel.dispatchIntent(FoodEatenListIntent.CreateEntry) },
                )
            },
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<FoodEatenListViewModel> { parametersOf(foodId) }
        FoodEatenList(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}