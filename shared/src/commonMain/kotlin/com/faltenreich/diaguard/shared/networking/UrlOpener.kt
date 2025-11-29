package com.faltenreich.diaguard.shared.networking

expect class UrlOpener constructor() {

    fun open(url: String)
}