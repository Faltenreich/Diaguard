package com.faltenreich.diaguard.tag.form

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.NavigationEvent
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TagFormViewModelTest : TestSuite {

    private val viewModel: TagFormViewModel by inject()
    private val navigation: Navigation by inject()
    private val tagRepository: TagRepository by inject()

    @Test
    fun `close modal when intending to close`() = runTest {
        navigation.events.test {
            viewModel.handleIntent(TagFormIntent.Close)
            assertTrue(awaitItem() is NavigationEvent.CloseModal)
        }
    }

    @Test
    fun `close modal when intending to submit and succeeding`() = runTest {
        navigation.events.test {
            viewModel.name = "name"
            viewModel.handleIntent(TagFormIntent.Submit)
            assertTrue(awaitItem() is NavigationEvent.CloseModal)
            assertNull(viewModel.error)
        }
    }

    @Test
    fun `show error when intending to submit the same tag twice`() = runTest {
        val name = "name"

        tagRepository.create(Tag.User(name = name))

        viewModel.name = name
        viewModel.handleIntent(TagFormIntent.Submit)

        assertNotNull(viewModel.error)
    }
}