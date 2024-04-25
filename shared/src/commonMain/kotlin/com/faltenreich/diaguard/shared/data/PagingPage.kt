package com.faltenreich.diaguard.shared.data

data class PagingPage(
    val page: Long,
    val pageSize: Long = 5,
) {

    operator fun plus(page: Long) = copy(page = this.page + page)

    operator fun minus(page: Long) = copy(page = this.page - page)

    operator fun inc() = plus(1)

    operator fun dec() = minus(1)

    companion object {

        val Zero = PagingPage(page = 0)
    }
}