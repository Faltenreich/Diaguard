package com.faltenreich.diaguard.tag.list

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.data.tag.TagRepository
import com.faltenreich.diaguard.data.navigation.Navigation
import com.faltenreich.diaguard.data.navigation.NavigationEvent
import com.faltenreich.diaguard.tag.detail.TagDetailScreen
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TagListViewModelTest : TestSuite() {

    private val viewModel: TagListViewModel by inject()
    private val navigation: Navigation by inject()
    private val tagRepository: TagRepository by inject()

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        tagRepository.create(TAG)
    }

    @Test
    fun `return all tags`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = 1,
                actual = awaitItem().tags.size,
            )
        }
    }

    @Test
    fun `open modal when intending to create tag`() = runTest {
        viewModel.state.test {
            viewModel.handleIntent(TagListIntent.OpenFormDialog)
            assertNotNull(awaitItem().formDialog)
        }
    }

    @Test
    fun `push screen when intending to open tag`() = runTest {
        val tag = checkNotNull(tagRepository.getByName(TAG.name))

        navigation.events.test {
            viewModel.handleIntent(TagListIntent.OpenTag(tag))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is TagDetailScreen)
        }
    }
    @Test
    fun `close modal when intending to close`() = runTest {
        viewModel.state.test {
            viewModel.handleIntent(TagListIntent.OpenFormDialog)
            viewModel.handleIntent(TagListIntent.CloseFormDialog)
            assertNull(awaitItem().formDialog)
        }
    }

    @Test
    fun `close modal when intending to submit and succeeding`() = runTest {
        viewModel.handleIntent(TagListIntent.StoreTag("a"))

        viewModel.state.test {
            assertNull(awaitItem().formDialog)
        }
    }

    @Test
    fun `show error when intending to submit the same tag twice`() = runTest {
        val name = "b"

        tagRepository.create(Tag.User(name = name))
        viewModel.handleIntent(TagListIntent.StoreTag(name))

        viewModel.state.test {
            assertNotNull(awaitItem().formDialog!!.error)
        }
    }

    companion object {

        private val TAG = Tag.User(name = "name")
    }
}