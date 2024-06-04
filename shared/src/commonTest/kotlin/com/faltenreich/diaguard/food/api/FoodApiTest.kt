package com.faltenreich.diaguard.food.api

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.shared.data.PagingPage
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class FoodApiTest : TestSuite {

    private val api: FoodApi by inject()

    @Test
    fun `searches for food`() = runTest {
        val response = api.search("", PagingPage(page = 0, pageSize = 10))
        assertTrue(response.isNotEmpty())
    }
}