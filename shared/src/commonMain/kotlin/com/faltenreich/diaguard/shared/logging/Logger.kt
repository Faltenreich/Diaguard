package com.faltenreich.diaguard.shared.logging

expect object Logger {

    fun verbose(message: String, throwable: Throwable? = null)

    fun debug(message: String, throwable: Throwable? = null)

    fun info(message: String, throwable: Throwable? = null)

    fun warning(message: String, throwable: Throwable? = null)

    fun error(message: String, throwable: Throwable? = null)
}