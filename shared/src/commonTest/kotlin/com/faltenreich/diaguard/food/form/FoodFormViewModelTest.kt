package com.faltenreich.diaguard.food.form

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodFactory
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

        val foodId = foodRepository.create(FoodFactory.createByUser())
        food = checkNotNull(foodRepository.getById(foodId))
    }

    @Test
    fun `show empty input when launching without food`() = runTest {
        viewModel = get(parameters = { parametersOf(null) })

        viewModel.state.test {
            val state = awaitItem()
            with(state.input) {
                assertEquals(expected = "", name)
                assertEquals(expected = "", brand)
                assertEquals(expected = "", ingredients)
                assertEquals(expected = "", labels)
                assertEquals(expected = "", carbohydrates)
                assertEquals(expected = "", energy)
                assertEquals(expected = "", fat)
                assertEquals(expected = "", fatSaturated)
                assertEquals(expected = "", fiber)
                assertEquals(expected = "", proteins)
                assertEquals(expected = "", salt)
                assertEquals(expected = "", sodium)
                assertEquals(expected = "", sugar)
            }
        }
    }

    @Test
    fun `show input when launching with food`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

        viewModel.state.test {
            val state = awaitItem()
            with(state.input) {
                assertEquals(expected = "name", name)
                assertEquals(expected = "brand", brand)
                assertEquals(expected = "ingredients", ingredients)
                assertEquals(expected = "labels", labels)
                assertEquals(expected = "20", carbohydrates)
                assertEquals(expected = "1", energy)
                assertEquals(expected = "2", fat)
                assertEquals(expected = "3", fatSaturated)
                assertEquals(expected = "4", fiber)
                assertEquals(expected = "5", proteins)
                assertEquals(expected = "6", salt)
                assertEquals(expected = "7", sodium)
                assertEquals(expected = "8", sugar)
            }
        }
    }

    @Test
    fun `show nutrients matching input in correct order`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

        viewModel.state.test {
            val state = awaitItem()
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
                actual = state.input.nutrients,
            )
        }
    }

    @Test
    fun `edit nutrient`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

        viewModel.state.test {
            val state = awaitItem()
            val nutrients = state.input.nutrients

            FoodNutrient.entries.forEachIndexed { index, nutrient ->
                val per100g = index.toString()
                val data = nutrients
                    .first { it.nutrient == nutrient }
                    .copy(per100g = per100g)
                viewModel.handleIntent(FoodFormIntent.SetNutrient(data))

                with(awaitItem().input) {
                    assertEquals(
                        expected = data.per100g,
                        actual = when (nutrient) {
                            FoodNutrient.CARBOHYDRATES -> carbohydrates
                            FoodNutrient.ENERGY -> energy
                            FoodNutrient.FAT -> fat
                            FoodNutrient.FAT_SATURATED-> fatSaturated
                            FoodNutrient.FIBER -> fiber
                            FoodNutrient.PROTEINS -> proteins
                            FoodNutrient.SALT -> salt
                            FoodNutrient.SODIUM -> sodium
                            FoodNutrient.SUGAR -> sugar
                        },
                    )
                }
            }
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

        turbineScope {
            val state = viewModel.state.testIn(backgroundScope)
            val events = navigation.events.testIn(backgroundScope)

            val name = "update"

            viewModel.handleIntent(FoodFormIntent.SetInput(state.awaitItem().input.copy(name = name)))
            viewModel.handleIntent(FoodFormIntent.Submit)

            assertTrue(events.awaitItem() is NavigationEvent.PopScreen)
            assertEquals(
                expected = name,
                actual = foodRepository.getById(food.id)?.name,
            )
        }
    }

    @Test
    fun `show snackbar when intending to submit and failing`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

        viewModel.state.test {
            val name = ""

            viewModel.handleIntent(FoodFormIntent.SetInput(awaitItem().input.copy(name = name)))
            viewModel.handleIntent(FoodFormIntent.Submit)

            awaitItem()

            assertEquals(
                expected = "food_form_missing_input",
                actual = awaitItem().error,
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `pop screen when intending to delete food`() = runTest {
        viewModel = get(parameters = { parametersOf(food.id) })

        navigation.events.test {
            viewModel.handleIntent(FoodFormIntent.Delete)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PopScreen)
            assertNull(foodRepository.getById(food.id))
        }
    }

    @Test
    fun `do nothing when intending to delete nothing`() = runTest {
        viewModel = get(parameters = { parametersOf(null) })

        navigation.events.test {
            viewModel.handleIntent(FoodFormIntent.Delete)
            ensureAllEventsConsumed()
        }
    }
}