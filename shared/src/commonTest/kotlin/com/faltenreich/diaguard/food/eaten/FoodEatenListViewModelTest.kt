package com.faltenreich.diaguard.food.eaten

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodFactory
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListIntent
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListState
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListViewModel
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import kotlinx.coroutines.test.runTest
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FoodEatenListViewModelTest : TestSuite {

    private val foodRepository: FoodRepository by inject()
    private val entryRepository: EntryRepository by inject()
    private val foodEatenRepository: FoodEatenRepository by inject()
    private val navigation: Navigation by inject()
    private val dateTimeFactory: DateTimeFactory by inject()

    private lateinit var viewModel: FoodEatenListViewModel

    private lateinit var food: Food.Local
    private lateinit var entry: Entry.Local

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()

        val foodId = foodRepository.create(FoodFactory.createByUser())
        food = foodRepository.getById(foodId)!!

        val entryId = entryRepository.create(
            Entry.User(
                dateTime = dateTimeFactory.now(),
                note = null,
            )
        )
        entry = entryRepository.getById(entryId)!!

        val foodEaten = FoodEaten.Intermediate(
            amountInGrams = 10.0,
            food = food,
            entry = entry,
        )
        foodEatenRepository.create(foodEaten)

        viewModel = get(parameters = { parametersOf(food.id) })
    }

    @Test
    fun `show food eaten for food`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state is FoodEatenListState.NonEmpty)

            val foodEaten = state.results.first().local
            assertEquals(
                expected = foodEaten.food,
                actual = food,
            )
            assertEquals(
                expected = foodEaten.entry,
                actual = entry,
            )
        }
    }

    @Test
    fun `open entry form when creating entry`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodEatenListIntent.CreateEntry)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntryFormScreen)
        }
    }

    @Test
    fun `open entry form when opening entry`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(FoodEatenListIntent.OpenEntry(entry))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntryFormScreen)
        }
    }
}