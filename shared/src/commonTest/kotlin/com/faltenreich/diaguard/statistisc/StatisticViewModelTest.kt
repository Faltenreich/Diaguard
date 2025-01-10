package com.faltenreich.diaguard.statistisc

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.picker.DateRangePickerModal
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.statistic.StatisticIntent
import com.faltenreich.diaguard.statistic.StatisticViewModel
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StatisticViewModelTest : TestSuite {

    private val viewModel: StatisticViewModel by inject()
    private val navigation: Navigation by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `launch with first category selected`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(
                expected = state.categories.first(),
                actual = state.selectedCategory,
            )
        }
    }

    @Test
    fun `return average for zero values`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = "0",
                actual = awaitItem().average.countPerDay,
            )
        }
    }

    @Test
    fun `update selected category when intending to select category`() = runTest {
        viewModel.state.test {
            val category = awaitItem().categories.last()

            viewModel.handleIntent(StatisticIntent.SetCategory(category))

            assertEquals(
                expected = category,
                actual = awaitItem().selectedCategory,
            )
        }
    }

    @Test
    fun `open modal when intending to set date range`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(StatisticIntent.OpenDateRangePicker)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is DateRangePickerModal)
        }
    }
}