package com.faltenreich.diaguard.preference.overview

import app.cash.turbine.test
import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertContentEquals

class OverviewPreferenceViewModelTest : TestSuite {

    private val viewModel: OverviewPreferenceViewModel by inject()

    @Test
    fun `show preferences`() = runTest {
        viewModel.state.test {
            assertContentEquals(
                expected = listOf(
                    "color_scheme",
                    "start_screen",
                    "decimal_places",
                    "data",
                    "measurement_categories",
                    "tags",
                    "food",
                    "contact",
                    "homepage",
                    "mail",
                    "facebook",
                    "about",
                    "source_code",
                    "licenses",
                    "privacy_policy",
                    "terms_and_conditions",
                    "version",
                ),
                actual = awaitItem().map(PreferenceListItem::title),
            )
        }
    }
}