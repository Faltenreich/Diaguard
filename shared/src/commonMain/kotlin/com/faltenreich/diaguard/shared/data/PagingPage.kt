package com.faltenreich.diaguard.shared.data

data class PagingPage(
    val page: Int,
    val pageSize: Int,
) {

    operator fun plus(page: Int) = copy(page = this.page + page)

    operator fun minus(page: Int) = copy(page = this.page - page)

    operator fun inc() = plus(1)

    operator fun dec() = minus(1)
}