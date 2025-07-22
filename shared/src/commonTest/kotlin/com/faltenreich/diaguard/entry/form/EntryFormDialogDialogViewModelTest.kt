package com.faltenreich.diaguard.entry.form

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.FoodFactory
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EntryFormDialogDialogViewModelTest : TestSuite {

    private val navigation: Navigation by inject()
    private val entryRepository: EntryTagRepository by inject()
    private val valueRepository: MeasurementValueRepository by inject()
    private val foodRepository: FoodRepository by inject()
    private val dateTimeFactory: DateTimeFactory by inject()

    private lateinit var viewModel: EntryFormViewModel

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    // TODO: Set locale
    @Test
    @Ignore
    fun `format date`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        val date = dateTimeFactory.date(1979, 1, 1)
        viewModel.handleIntent(EntryFormIntent.SetDate(date))

        viewModel.state.test {
            assertEquals(
                expected = "01.01.1979",
                actual = awaitItem().dateTime.dateLocalized,
            )
        }
    }

    @Test
    fun `format time`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        val time = dateTimeFactory.time(12, 12, 12)
        viewModel.handleIntent(EntryFormIntent.SetTime(time))

        viewModel.state.test {
            assertEquals(
                expected = "12:12",
                actual = awaitItem().dateTime.timeLocalized,
            )
        }
    }

    @Test
    fun `launch with no and then some categories`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        viewModel.state.test {
            assertTrue(awaitItem().measurements.isEmpty())
            assertTrue(awaitItem().measurements.isNotEmpty())
        }
    }

    @Test
    fun `launch with tags`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        viewModel.state.test {
            assertTrue(awaitItem().tags.suggestions.isNotEmpty())
        }
    }

    @Test
    fun `update tag selection when adding tag`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        val tag = Tag.User(name = "tag")
        viewModel.handleIntent(EntryFormIntent.AddTag(tag))

        viewModel.state.test {
            assertTrue(awaitItem().tags.selection.contains(tag))
        }
    }

    @Test
    fun `update tag selection when removing tag`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        val tag = Tag.User(name = "tag")
        viewModel.handleIntent(EntryFormIntent.AddTag(tag))
        viewModel.handleIntent(EntryFormIntent.RemoveTag(tag))

        viewModel.state.test {
            assertFalse(awaitItem().tags.selection.contains(tag))
        }
    }

    @Test
    fun `update measurements when editing input`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        viewModel.state.test {
            awaitItem()

            val input = awaitItem()
                .measurements.first()
                .propertyInputStates.first()
                .copy(input = "10")
            viewModel.handleIntent(EntryFormIntent.Edit(input))

            assertEquals(
                expected = input,
                actual = awaitItem().measurements.first().propertyInputStates.first(),
            )
        }
    }

    @Test
    fun `store entry and pop screen when submitting with success`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        turbineScope {
            val state = viewModel.state.testIn(backgroundScope)
            val navigation = navigation.events.testIn(backgroundScope)

            state.awaitItem()

            val input = state.awaitItem()
                .measurements.first()
                .propertyInputStates.first()
                .copy(input = "10")
            viewModel.handleIntent(EntryFormIntent.Edit(input))

            state.awaitItem()

            viewModel.handleIntent(EntryFormIntent.Submit)

            val values = valueRepository.getByEntryId(entryRepository.getLastId()!!)
            assertEquals(
                expected = 10.0,
                actual = values.first().value,
            )
            assertTrue(navigation.awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `show modal if deleting entry`() = runTest {
        storeValue(10.0, DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        val entryId = entryRepository.getLastId()!!
        viewModel = get(parameters = { parametersOf(entryId, null, null) })

        viewModel.state.test {
            viewModel.handleIntent(EntryFormIntent.Delete(needsConfirmation = true))
            assertNotNull(awaitItem().deleteDialog)
        }
    }

    @Test
    fun `pop screen if deleting non-existing entry`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        navigation.events.test {
            viewModel.handleIntent(EntryFormIntent.Delete(needsConfirmation = true))
            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `open food search when selecting food`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        navigation.events.test {
            viewModel.handleIntent(EntryFormIntent.SelectFood)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is FoodSearchScreen)
        }
    }

    // FIXME
    @Ignore
    @Test
    fun `update food eaten when adding food`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        viewModel.state.test {
            val foodId = foodRepository.create(FoodFactory.createByUser())
            val food = checkNotNull(foodRepository.getById(foodId))
            viewModel.handleIntent(EntryFormIntent.AddFood(food))

            assertEquals(
                expected = food,
                actual = awaitItem().foodEaten.first().food,
            )
        }
    }

    // FIXME
    @Ignore
    @Test
    fun `update food eaten when editing food`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        viewModel.state.test {
            val foodId = foodRepository.create(FoodFactory.createByUser())
            val food = checkNotNull(foodRepository.getById(foodId))
            viewModel.handleIntent(EntryFormIntent.AddFood(food))

            val update = awaitItem().foodEaten.first().copy(amountInGrams = "10.0")

            viewModel.handleIntent(EntryFormIntent.EditFood(update))

            assertEquals(
                expected = update,
                actual = awaitItem().foodEaten.first(),
            )
        }
    }

    // FIXME
    @Ignore
    @Test
    fun `update food eaten when removing food`() = runTest {
        viewModel = get(parameters = { parametersOf(null, null, null) })

        viewModel.state.test {
            val foodId = foodRepository.create(FoodFactory.createByUser())
            val food = checkNotNull(foodRepository.getById(foodId))
            viewModel.handleIntent(EntryFormIntent.AddFood(food))

            val foodInput = awaitItem().foodEaten.first()

            viewModel.handleIntent(EntryFormIntent.RemoveFood(foodInput))

            assertTrue(awaitItem().foodEaten.isEmpty())
        }
    }
}