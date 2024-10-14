package com.faltenreich.diaguard.shared.logging

class ConsoleLogger : Logger {

    override fun verbose(message: String, throwable: Throwable?) {
        println("V: $message ${throwable?.toString()}")
    }

    override fun debug(message: String, throwable: Throwable?) {
        println("D: $message ${throwable?.toString()}")
    }

    override fun info(message: String, throwable: Throwable?) {
        println("I: $message ${throwable?.toString()}")
    }

    override fun warning(message: String, throwable: Throwable?) {
        println("W: $message ${throwable?.toString()}")
    }

    override fun error(message: String, throwable: Throwable?) {
        println("E: $message ${throwable?.toString()}")
    }
}