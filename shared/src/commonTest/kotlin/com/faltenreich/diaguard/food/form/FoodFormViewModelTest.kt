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
import kotlin.test.assertContentEquals
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
    }

    @Test
    fun `show empty input when launching without food`() {
        viewModel = get(parameters = { parametersOf(null) })

        assertEquals(expected = "", viewModel.name)
        assertEquals(expected = "", viewModel.brand)
        assertEquals(expected = "", viewModel.ingredients)
        assertEquals(expected = "", viewModel.labels)

        assertEquals(expected = "", viewModel.carbohydrates)
        assertEquals(expected = "", viewModel.energy)
        assertEquals(expected = "", viewModel.fat)
        assertEquals(expected = "", viewModel.fatSaturated)
        assertEquals(expected = "", viewModel.fiber)
        assertEquals(expected = "", viewModel.proteins)
        assertEquals(expected = "", viewModel.salt)
        assertEquals(expected = "", viewModel.sodium)
        assertEquals(expected = "", viewModel.sugar)
        }

    @Test
    fun `show input when launching with food`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

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
    fun `show nutrients matching input in correct order`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

        assertContentEquals(
            expected = listOf(
                FoodNutrientData(
                    nutrient = FoodNutrient.CARBOHYDRATES,
                    per100g = "20",
                    isLast = false,
                ),
                FoodNutrientData(
                    nutrient = FoodNutrient.SUGAR,
                    per100g = "8",
                    isLast = false,
                ),
                FoodNutrientData(
                    nutrient = FoodNutrient.ENERGY,
                    per100g = "1",
                    isLast = false,
                ),
                FoodNutrientData(
                    nutrient = FoodNutrient.FAT,
                    per100g = "2",
                    isLast = false,
                ),
                FoodNutrientData(
                    nutrient = FoodNutrient.FAT_SATURATED,
                    per100g = "3",
                    isLast = false,
                ),
                FoodNutrientData(
                    nutrient = FoodNutrient.FIBER,
                    per100g = "4",
                    isLast = false,
                ),
                FoodNutrientData(
                    nutrient = FoodNutrient.PROTEINS,
                    per100g = "5",
                    isLast = false,
                ),
                FoodNutrientData(
                    nutrient = FoodNutrient.SALT,
                    per100g = "6",
                    isLast = false,
                ),
                FoodNutrientData(
                    nutrient = FoodNutrient.SODIUM,
                    per100g = "7",
                    isLast = true,
                ),
            ),
            actual = viewModel.nutrientData,
        )
    }

    @Test
    fun `edit nutrient`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

        FoodNutrient.entries.forEachIndexed { index, nutrient ->
            val per100g = index.toString()
            val data = viewModel.nutrientData
                .first { it.nutrient == nutrient }
                .copy(per100g = per100g)
            viewModel.handleIntent(FoodFormIntent.EditNutrient(data))

            assertEquals(
                expected = data.per100g,
                actual = when (nutrient) {
                    FoodNutrient.CARBOHYDRATES -> viewModel.carbohydrates
                    FoodNutrient.ENERGY -> viewModel.energy
                    FoodNutrient.FAT -> viewModel.fat
                    FoodNutrient.FAT_SATURATED-> viewModel.fatSaturated
                    FoodNutrient.FIBER -> viewModel.fiber
                    FoodNutrient.PROTEINS -> viewModel.proteins
                    FoodNutrient.SALT -> viewModel.salt
                    FoodNutrient.SODIUM -> viewModel.sodium
                    FoodNutrient.SUGAR -> viewModel.sugar
                },
            )
        }
    }

    @Test
    fun `push screen when intending to open food eaten`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

        navigation.events.test {
            viewModel.handleIntent(FoodFormIntent.OpenFoodEaten(food))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is FoodEatenListScreen)
        }
    }

    @Test
    fun `update food and pop screen when intending to submit and succeeding`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

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
        viewModel = get(parameters = { parametersOf(food.id) })

        navigation.events.test {
            val name = ""
            viewModel.name = name

            viewModel.handleIntent(FoodFormIntent.Submit)

            assertTrue(awaitItem() is NavigationEvent.ShowSnackbar)
        }
    }

    @Test
    fun `delete food pop screen when intending to delete food`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

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