package com.faltenreich.diaguard.navigation.system

import com.faltenreich.diaguard.networking.UrlOpener

class OpenUrlUseCase(private val urlOpener: UrlOpener) {

    operator fun invoke(url: String) {
        urlOpener.open(url)
    }
}