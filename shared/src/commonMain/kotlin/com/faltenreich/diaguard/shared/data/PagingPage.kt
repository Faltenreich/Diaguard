package com.faltenreich.diaguard.shared.data

data class PagingPage(
    val page: Long,
    val pageSize: Long = 5,
) {

    companion object {

        val Zero = PagingPage(page = 0)
    }
}