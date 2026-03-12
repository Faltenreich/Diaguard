package com.faltenreich.diaguard.food.api

import com.faltenreich.diaguard.data.food.api.FoodApi
import com.faltenreich.diaguard.food.foodModule
import com.faltenreich.diaguard.test.TestSuite
import com.faltenreich.diaguard.view.paging.PagingPage
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class FoodApiTest : TestSuite(foodModule()) {

    private val api: FoodApi by inject()

    @Test
    fun `search returns food`() = runTest {
        val response = api.search("", PagingPage(page = 0, pageSize = 10))
        assertTrue(response.isNotEmpty())
    }
}