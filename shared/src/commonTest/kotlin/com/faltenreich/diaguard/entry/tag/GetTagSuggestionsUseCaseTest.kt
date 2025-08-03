package com.faltenreich.diaguard.entry.tag

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.form.tag.GetTagSuggestionsUseCase
import com.faltenreich.diaguard.tag.Tag
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertContentEquals

class GetTagSuggestionsUseCaseTest : TestSuite {

    private val useCase = GetTagSuggestionsUseCase()
    private val dateTimeFactory by inject<DateTimeFactory>()

    @Test
    fun `filter suggestions by query`() = runTest {
        val actual = useCase(
            tags = listOf(
                Tag.User(name = "a"),
                Tag.User(name = "b"),
            ),
            selection = emptyList(),
            query = "a",
        )
        assertContentEquals(
            expected = listOf(
                Tag.User(name = "a"),
            ),
            actual = actual,
        )
    }

    @Test
    fun `remove selection`() = runTest {
        val actual = useCase(
            tags = listOf(
                Tag.User(name = "a"),
                Tag.User(name = "b"),
            ),
            selection = listOf(
                Tag.User(name = "a"),
                Tag.Local(
                    id = 0L,
                    createdAt = dateTimeFactory.now(),
                    updatedAt = dateTimeFactory.now(),
                    name = "b",
                ),
            ),
            query = "a",
        )
        assertContentEquals(
            expected = emptyList(),
            actual = actual,
        )
    }
}