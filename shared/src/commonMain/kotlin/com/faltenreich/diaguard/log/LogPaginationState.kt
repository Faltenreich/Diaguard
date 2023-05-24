package com.faltenreich.diaguard.log

data class LogPaginationState(
    val page: Int = 0,
    val pageSize: Int = PAGE_SIZE,
) {

    companion object {

        private const val PAGE_SIZE = 30
    }
}