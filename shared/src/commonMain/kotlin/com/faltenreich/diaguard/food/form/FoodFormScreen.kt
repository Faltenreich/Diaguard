package com.faltenreich.diaguard.food.form

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.view.FloatingActionButton
import diaguard.core.view.generated.resources.ic_check
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food
import diaguard.shared.generated.resources.food_delete
import diaguard.shared.generated.resources.food_eaten
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.ic_history
import diaguard.shared.generated.resources.save
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class FoodFormScreen(private val foodId: Long) : Screen {

    constructor(food: Food.Local? = null) : this(foodId = food?.id ?: -1)

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
                FloatingActionButton(
                    painter = painterResource(diaguard.core.view.generated.resources.Res.drawable.ic_check),
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