package com.faltenreich.diaguard.entry.form

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.picker.DatePickerModal
import com.faltenreich.diaguard.datetime.picker.TimePickerModal
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EntryFormViewModelTest : TestSuite {

    private val viewModel: EntryFormViewModel by inject(
        parameters = { parametersOf(null, null, null) },
    )
    private val navigation: Navigation by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
    }

    @Test
    fun `opens dialog when selecting date`() = runTest {
        viewModel.handleIntent(EntryFormIntent.SelectDate)
        assertTrue(navigation.modal.value is DatePickerModal)
    }

    @Test
    fun `opens dialog when selecting time`() = runTest {
        viewModel.handleIntent(EntryFormIntent.SelectTime)
        assertTrue(navigation.modal.value is TimePickerModal)
    }

    @Test
    fun `opens food search when selecting food`() = runTest {
        viewModel.handleIntent(EntryFormIntent.SelectFood)
        // TODO: assertTrue(navigation.currentScreen.value is FoodSearchScreen)
    }

    @Test
    fun `suggests tags`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.tags.isNotEmpty())
        }
    }

    @Test
    fun `adds tag to selection`() = runTest {
        val tag = Tag.User(name = "tag")
        viewModel.handleIntent(EntryFormIntent.AddTag(tag))

        viewModel.tagSelection.test {
            val tags = awaitItem()
            assertTrue(tags.contains(tag))
        }
    }

    @Test
    fun `removes tag from selection`() = runTest {
        val tag = Tag.User(name = "tag")
        viewModel.handleIntent(EntryFormIntent.AddTag(tag))
        viewModel.handleIntent(EntryFormIntent.RemoveTag(tag))

        viewModel.tagSelection.test {
            val tags = awaitItem()
            assertFalse(tags.contains(tag))
        }
    }
}