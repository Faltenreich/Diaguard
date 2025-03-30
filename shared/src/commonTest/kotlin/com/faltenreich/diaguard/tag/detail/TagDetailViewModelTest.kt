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
import org.koin.test.get
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TagDetailViewModelTest : TestSuite {

    private val navigation: Navigation by inject()
    private val entryRepository: EntryRepository by inject()
    private val tagRepository: TagRepository by inject()

    private lateinit var viewModel: TagDetailViewModel
    private lateinit var tag: Tag.Local

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()

        importSeed()

        val tagId = tagRepository.create(TAG_BY_USER)
        tag = checkNotNull(tagRepository.getById(tagId))
        viewModel = get(parameters = { parametersOf(tagId) })
    }

    @Test
    fun `pop screen tag when intending to update tag with new and unique name`() = runTest {
        navigation.events.test {
            viewModel.name = "update"
            viewModel.handleIntent(TagDetailIntent.UpdateTag)

            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `pop screen tag when intending to update tag with same but unique name`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(TagDetailIntent.UpdateTag)

            assertTrue(awaitItem() is NavigationEvent.PopScreen)
        }
    }

    @Test
    fun `show error when intending to update tag with same name but redundant name`() = runTest {
        val name = "update"
        tagRepository.create(Tag.User(name = name))

        viewModel.name = name
        viewModel.handleIntent(TagDetailIntent.UpdateTag)

        assertNotNull(viewModel.error)
    }

    @Test
    fun `open modal when intending to delete`() = runTest {
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

        private val TAG_BY_USER = Tag.User(name = "name")
    }
}