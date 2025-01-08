package com.faltenreich.diaguard.food.search

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.food.FoodFactory
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.form.FoodFormScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.preference.food.FoodPreferenceListScreen
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class FoodSearchViewModelTest : TestSuite {

    private val viewModel: FoodSearchViewModel by inject(parameters = { parametersOf(FoodSearchMode.FIND) })
    private val navigation: Navigation by inject()
    private val foodRepository: FoodRepository by inject()

    @Test
    fun `pop screen when intending to close`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodSearchIntent.Close)
            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `push screen when intending to create food`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodSearchIntent.Create)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is FoodFormScreen)
        }
    }

    @Test
    fun `push screen when intending to open food`() = runTest {
        val foodId = foodRepository.create(FoodFactory.createByUser())
        val food = requireNotNull(foodRepository.getById(foodId))

        navigation.events.test {
            viewModel.handleIntent(FoodSearchIntent.OpenFood(food))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is FoodFormScreen)
        }
    }

    @Test
    fun `push screen when intending to open preferences`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodSearchIntent.OpenPreferences)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is FoodPreferenceListScreen)
        }
    }
}