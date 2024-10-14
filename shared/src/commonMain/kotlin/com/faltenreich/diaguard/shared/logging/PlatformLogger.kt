package com.faltenreich.diaguard.shared.logging

expect class PlatformLogger constructor() : Logger {

    override fun verbose(message: String, throwable: Throwable?)

    override fun debug(message: String, throwable: Throwable?)

    override fun info(message: String, throwable: Throwable?)

    override fun warning(message: String, throwable: Throwable?)

    override fun error(message: String, throwable: Throwable?)
}