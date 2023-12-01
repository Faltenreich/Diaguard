package com.faltenreich.diaguard.shared.logging

import android.util.Log

actual object Logger {

    private const val CALLS_UNTIL_CALL_SITE = 5

    private fun tracing(message: String, traced: (tag: String, message: String) -> Unit) {
        val stackTrace = Thread.currentThread().stackTrace
        val callSite = stackTrace[CALLS_UNTIL_CALL_SITE]
        val tag = callSite.className.substringAfterLast(".")
        val hyperlink = callSite.toString()
            .substringAfterLast("(")
            .substringBeforeLast(")")
        traced(tag, "$message ($hyperlink)")
    }

    actual fun verbose(message: String, throwable: Throwable?) = tracing(message) { tag, msg ->
        Log.v(tag, msg, throwable)
    }

    actual fun debug(message: String, throwable: Throwable?) = tracing(message) { tag, msg ->
        Log.d(tag, msg, throwable)
    }

    actual fun info(message: String, throwable: Throwable?) = tracing(message) { tag, msg ->
        Log.i(tag, msg, throwable)
    }

    actual fun warning(message: String, throwable: Throwable?) = tracing(message) { tag, msg ->
        Log.w(tag, msg, throwable)
    }

    actual fun error(message: String, throwable: Throwable?) = tracing(message) { tag, msg ->
        Log.e(tag, msg, throwable)
    }
}