package com.faltenreich.diaguard.shared.data

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class PagingPageTest {

    @Test
    fun `plus adds value to page`() {
        val page = PagingPage(0, 0)
        val value = Random.nextInt(0, Int.MAX_VALUE)
        val result = page + value
        assertEquals(page.page + value, result.page)
    }

    @Test
    fun `minus subtracts value to page`() {
        val page = PagingPage(0, 0)
        val value = Random.nextInt(0, Int.MAX_VALUE)
        val result = page - value
        assertEquals(page.page - value, result.page)
    }

    @Test
    fun `inc increments page by one`() {
        var page = PagingPage(0, 0)
        page++
        assertEquals(1, page.page)
    }

    @Test
    fun `dec decrements page by one`() {
        var page = PagingPage(1, 0)
        page--
        assertEquals(0, page.page)
    }
}