package com.faltenreich.diaguard.system.web

class OpenUrlUseCase(private val urlOpener: UrlOpener) {

    operator fun invoke(url: String) {
        urlOpener.open(url)
    }
}