package com.faltenreich.diaguard.tag.list

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository
import com.faltenreich.diaguard.tag.detail.TagDetailScreen
import com.faltenreich.diaguard.tag.form.TagFormModal
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TagListViewModelTest : TestSuite {

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
        navigation.events.test {
            viewModel.handleIntent(TagListIntent.CreateTag)

            val event = awaitItem()
            assertTrue(event is NavigationEvent.OpenModal)
            assertTrue(event.modal is TagFormModal)
        }
    }

    @Test
    fun `push screen when intending to open tag`() = runTest {
        val tag = requireNotNull(tagRepository.getByName(TAG.name))

        navigation.events.test {
            viewModel.handleIntent(TagListIntent.OpenTag(tag))

            val event = awaitItem()
            assertTrue(event is NavigationEvent.PushScreen)
            assertTrue(event.screen is TagDetailScreen)
        }
    }

    companion object {

        private val TAG = Tag.User(name = "name")
    }
}