package com.faltenreich.diaguard.networking

expect class UrlOpener constructor() {

    fun open(url: String)
}