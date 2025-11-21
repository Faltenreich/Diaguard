package com.faltenreich.diaguard.system.web

expect class UrlOpener constructor() {

    fun open(url: String)
}