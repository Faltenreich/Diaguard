package com.faltenreich.diaguard.food.form

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FoodFormViewModelTest : TestSuite {

    private val navigation: Navigation by inject()
    private val foodRepository: FoodRepository by inject()

    private lateinit var viewModel: FoodFormViewModel
    private lateinit var food: Food.Local

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()

        val foodId = foodRepository.create(FOOD_BY_USER)
        food = requireNotNull(foodRepository.getById(foodId))
        viewModel = get(parameters = { parametersOf(foodId) })
    }

    @Test
    fun `push screen when intending to open food eaten`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodFormIntent.OpenFoodEaten(food))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is FoodEatenListScreen)
        }
    }

    @Test
    fun `delete food pop screen when intending to delete food`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodFormIntent.Delete)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PopScreen)
            assertNull(foodRepository.getById(food.id))
        }
    }

    companion object {

        private val FOOD_BY_USER = Food.User(
            name = "name",
            brand = null,
            ingredients = null,
            labels = null,
            carbohydrates = 0.0,
            energy = null,
            fat = null,
            fatSaturated = null,
            fiber = null,
            proteins = null,
            salt = null,
            sodium = null,
            sugar = null,
        )
    }
}