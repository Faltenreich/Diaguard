package com.faltenreich.diaguard.preference.food

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class FoodPreferenceViewModelTest : TestSuite {

    private val viewModel: FoodPreferenceViewModel by inject()

    @Test
    fun `show preferences`() = runTest {
        viewModel.state.test {
            assertEquals(
                expected = listOf(
                    "food_custom",
                    "food_custom_show",
                    "food_common",
                    "food_source_common_provider",
                    "food_common_show",
                    "food_branded",
                    "food_source_branded_provider",
                    "food_branded_show",
                ),
                actual = awaitItem().map(PreferenceListItem::title),
            )
        }
    }
}