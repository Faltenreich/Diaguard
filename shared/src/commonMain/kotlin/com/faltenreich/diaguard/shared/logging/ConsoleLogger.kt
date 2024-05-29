package com.faltenreich.diaguard.shared.logging

class ConsoleLogger : Logger {

    override fun verbose(message: String, throwable: Throwable?) {
        println("V: $message")
    }

    override fun debug(message: String, throwable: Throwable?) {
        println("D: $message")
    }

    override fun info(message: String, throwable: Throwable?) {
        println("I: $message")
    }

    override fun warning(message: String, throwable: Throwable?) {
        println("W: $message")
    }

    override fun error(message: String, throwable: Throwable?) {
        println("E: $message")
    }
}