package com.faltenreich.diaguard.shared.logging

import android.util.Log

actual object Logger {

    // Steps until call-site
    private const val STACK_TRACE_INDEX_CALLER = 5

    private val stackTraceElement: StackTraceElement
        get() = Thread.currentThread().stackTrace[STACK_TRACE_INDEX_CALLER]

    private val tag: String
        get() = stackTraceElement.className.substringAfterLast(".")

    private val hyperlink: String
        get() = stackTraceElement.toString()
            .substringAfterLast("(")
            .substringBeforeLast(")")

    actual fun verbose(message: String, throwable: Throwable?) {
        Log.v(tag, "$message ($hyperlink)", throwable)
    }

    actual fun debug(message: String, throwable: Throwable?) {
        Log.d(tag, "$message ($hyperlink)", throwable)
    }

    actual fun info(message: String, throwable: Throwable?) {
        Log.i(tag, "$message ($hyperlink)", throwable)
    }

    actual fun warning(message: String, throwable: Throwable?) {
        Log.w(tag, "$message ($hyperlink)", throwable)
    }

    actual fun error(message: String, throwable: Throwable?) {
        Log.e(tag, "$message ($hyperlink)", throwable)
    }
}