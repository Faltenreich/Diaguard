package com.faltenreich.diaguard.food.form

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.food.nutrient.FoodNutrient
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
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
    fun `launch with data for passed food`() = runTest {
        assertEquals(expected = "name", viewModel.name)
        assertEquals(expected = "brand", viewModel.brand)
        assertEquals(expected = "ingredients", viewModel.ingredients)
        assertEquals(expected = "labels", viewModel.labels)

        assertEquals(expected = "20", viewModel.carbohydrates)
        assertEquals(expected = "1", viewModel.energy)
        assertEquals(expected = "2", viewModel.fat)
        assertEquals(expected = "3", viewModel.fatSaturated)
        assertEquals(expected = "4", viewModel.fiber)
        assertEquals(expected = "5", viewModel.proteins)
        assertEquals(expected = "6", viewModel.salt)
        assertEquals(expected = "7", viewModel.sodium)
        assertEquals(expected = "8", viewModel.sugar)
    }

    @Test
    fun `edit nutrient`() = runTest {
        val data = FoodNutrientData(
            nutrient = FoodNutrient.CARBOHYDRATES,
            per100g = "100",
            isLast = false,
        )
        viewModel.handleIntent(FoodFormIntent.EditNutrient(data))

        assertEquals(
            expected = data.per100g,
            actual = viewModel.carbohydrates,
        )
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
    fun `update food and pop screen when intending to submit and succeeding`() = runTest {
        navigation.events.test {
            val name = "update"
            viewModel.name = name

            viewModel.handleIntent(FoodFormIntent.Submit)

            assertTrue(awaitItem() is NavigationEvent.PopScreen)
            assertEquals(
                expected = name,
                actual = foodRepository.getById(food.id)?.name,
            )
        }
    }

    @Test
    fun `show snackbar when intending to submit and failing`() = runTest {
        navigation.events.test {
            val name = ""
            viewModel.name = name

            viewModel.handleIntent(FoodFormIntent.Submit)

            assertTrue(awaitItem() is NavigationEvent.ShowSnackbar)
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
            brand = "brand",
            ingredients = "ingredients",
            labels = "labels",
            carbohydrates = 20.0,
            energy = 1.0,
            fat = 2.0,
            fatSaturated = 3.0,
            fiber = 4.0,
            proteins = 5.0,
            salt = 6.0,
            sodium = 7.0,
            sugar = 8.0,
        )
    }
}