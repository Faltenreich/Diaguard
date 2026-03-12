package com.faltenreich.diaguard.food.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.food
import com.faltenreich.diaguard.resource.food_delete
import com.faltenreich.diaguard.resource.food_eaten
import com.faltenreich.diaguard.resource.ic_check
import com.faltenreich.diaguard.resource.ic_delete
import com.faltenreich.diaguard.resource.ic_history
import com.faltenreich.diaguard.resource.save
import com.faltenreich.diaguard.view.bar.BottomAppBarItem
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class FoodFormScreen(private val foodId: Long) : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.food))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<FoodFormViewModel> {
            parametersOf(foodId.takeIf { it >= 0 })
        }
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = stringResource(Res.string.food_delete),
                    onClick = { viewModel.dispatchIntent(FoodFormIntent.Delete(needsConfirmation = true)) },
                )
                val food = viewModel.collectState()?.food
                if (food != null) {
                    BottomAppBarItem(
                        painter = painterResource(Res.drawable.ic_history),
                        contentDescription = stringResource(Res.string.food_eaten),
                        onClick = { viewModel.dispatchIntent(FoodFormIntent.OpenFoodEaten(food)) },
                    )
                }
            },
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(Res.drawable.ic_check),
                    contentDescription = stringResource(Res.string.save),
                    onClick = { viewModel.dispatchIntent(FoodFormIntent.Submit) },
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<FoodFormViewModel> {
            parametersOf(foodId.takeIf { it >= 0 })
        }
        FoodForm(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}