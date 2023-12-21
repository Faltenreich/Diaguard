package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.form.FoodForm
import com.faltenreich.diaguard.food.form.FoodFormIntent
import com.faltenreich.diaguard.food.form.FoodFormViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import dev.icerock.moko.resources.compose.painterResource
import org.koin.core.parameter.parametersOf

data class FoodFormScreen(val food: Food? = null) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.food))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<FoodFormViewModel> { parametersOf(food) }
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_delete),
                    contentDescription = MR.strings.food_delete,
                    onClick = { viewModel.dispatchIntent(FoodFormIntent.Delete) },
                )
                food?.let {
                    BottomAppBarItem(
                        painter = painterResource(MR.images.ic_history),
                        contentDescription = MR.strings.food_eaten,
                        onClick = { viewModel.dispatchIntent(FoodFormIntent.OpenFoodEaten(food)) },
                    )
                }
            },
            floatingActionButton = {
                val viewModel = getViewModel<FoodFormViewModel> { parametersOf(food) }
                FloatingActionButton(onClick = { viewModel.dispatchIntent(FoodFormIntent.Submit) }) {
                    Icon(
                        painter = painterResource(MR.images.ic_check),
                        contentDescription = getString(MR.strings.food_save),
                    )
                }
            }
        )

    @Composable
    override fun Content() {
        FoodForm(viewModel = getViewModel { parametersOf(food) })
    }
}