package com.faltenreich.diaguard.food.search

import app.cash.turbine.test
import com.faltenreich.diaguard.data.fake.FakeFactory
import com.faltenreich.diaguard.data.food.FoodRepository
import com.faltenreich.diaguard.data.navigation.Navigation
import com.faltenreich.diaguard.data.navigation.NavigationEvent
import com.faltenreich.diaguard.data.navigation.NavigationTarget
import com.faltenreich.diaguard.food.foodModule
import com.faltenreich.diaguard.test.TestSuite
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class FoodSearchViewModelTest : TestSuite(foodModule()) {

    private val viewModel: FoodSearchViewModel by inject(parameters = { parametersOf(FoodSearchMode.FIND) })
    private val navigation: Navigation by inject()
    private val foodRepository: FoodRepository by inject()

    @Test
    fun `pop screen when intending to close`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodSearchIntent.Close)
            assertTrue(awaitItem() is NavigationEvent.NavigateBack)
        }
    }

    @Test
    fun `push screen when intending to create food`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodSearchIntent.Create)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.NavigateTo)
            assertTrue(event.target is NavigationTarget.FoodForm)
        }
    }

    @Test
    fun `push screen when intending to open food`() = runTest {
        val foodId = foodRepository.create(FakeFactory.foodByUser())
        val food = checkNotNull(foodRepository.getById(foodId))

        navigation.events.test {
            viewModel.handleIntent(FoodSearchIntent.OpenFood(food))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.NavigateTo)
            assertTrue(event.target is NavigationTarget.FoodForm)
        }
    }

    @Test
    fun `push screen when intending to open preferences`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodSearchIntent.OpenPreferences)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.NavigateTo)
            assertTrue(event.target is NavigationTarget.FoodPreferenceList)
        }
    }
}