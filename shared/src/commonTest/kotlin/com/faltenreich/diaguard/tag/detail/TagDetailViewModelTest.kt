package com.faltenreich.diaguard.tag.detail

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.view.DeleteModal
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TagDetailViewModelTest : TestSuite {

    private val viewModel: TagDetailViewModel by inject(parameters = { parametersOf(1L) })
    private val navigation: Navigation by inject()
    private val entryRepository: EntryRepository by inject()
    private val tagRepository: TagRepository by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        importSeed()
        tagRepository.create(TAG)
    }

    @Test
    fun `pop screen tag when intending to update tag and succeeding`() = runTest {
        navigation.events.test {
            viewModel.name = "update"
            viewModel.handleIntent(TagDetailIntent.UpdateTag)

            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `show error when intending to update tag and failing`() = runTest {
        viewModel.name = TAG.name
        viewModel.handleIntent(TagDetailIntent.UpdateTag)

        assertNotNull(viewModel.error)
    }

    @Test
    fun `opens modal when intending to delete`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(TagDetailIntent.DeleteTag)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is DeleteModal)
        }
    }

    @Test
    fun `push screen when intending to open entry`() = runTest {
        storeValue(120.0, DatabaseKey.MeasurementProperty.BLOOD_SUGAR)
        val entry = entryRepository.getAll().first().first()

        navigation.events.test {
            viewModel.handleIntent(TagDetailIntent.OpenEntry(entry))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntryFormScreen)
        }
    }

    @Test
    fun `push screen when intending to open entry search`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(TagDetailIntent.OpenEntrySearch(query = ""))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is EntrySearchScreen)
        }
    }

    companion object {

        private val TAG = Tag.User(name = "name")
    }
}