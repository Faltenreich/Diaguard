package com.faltenreich.diaguard.shared.logging

class ConsoleLogger : Logger {

    private fun getMessage(message: String, throwable: Throwable?): String {
        return throwable?.let { "$message ($throwable)" } ?: message
    }

    override fun verbose(message: String, throwable: Throwable?) {
        println("V: ${getMessage(message, throwable)}")
    }

    override fun debug(message: String, throwable: Throwable?) {
        println("D: ${getMessage(message, throwable)}")
    }

    override fun info(message: String, throwable: Throwable?) {
        println("I: ${getMessage(message, throwable)}")
    }

    override fun warning(message: String, throwable: Throwable?) {
        println("W: ${getMessage(message, throwable)}")
    }

    override fun error(message: String, throwable: Throwable?) {
        println("E: ${getMessage(message, throwable)}")
    }
}