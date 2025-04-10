package com.faltenreich.diaguard.food.form

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food
import diaguard.shared.generated.resources.food_delete
import diaguard.shared.generated.resources.food_eaten
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.ic_delete
import diaguard.shared.generated.resources.ic_history
import diaguard.shared.generated.resources.save
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

@Serializable
data class FoodFormScreen(private val foodId: Long) : Screen {

    constructor(food: Food.Local? = null) : this(foodId = food?.id ?: -1)

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.food))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<FoodFormViewModel> { parametersOf(foodId) }
        return BottomAppBarStyle.Visible(
            actions = {
                val food = viewModel.food
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = Res.string.food_delete,
                    onClick = { viewModel.dispatchIntent(FoodFormIntent.Delete) },
                )
                if (food != null) {
                    BottomAppBarItem(
                        painter = painterResource(Res.drawable.ic_history),
                        contentDescription = Res.string.food_eaten,
                        onClick = { viewModel.dispatchIntent(FoodFormIntent.OpenFoodEaten(food)) },
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.dispatchIntent(FoodFormIntent.Submit) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = getString(Res.string.save),
                    )
                }
            }
        )
    }

    @Composable
    override fun Content() {
        FoodForm(viewModel = viewModel { parametersOf(foodId.takeIf { it >= 0 }) })
    }
}