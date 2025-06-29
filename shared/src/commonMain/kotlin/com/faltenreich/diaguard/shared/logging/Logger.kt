package com.faltenreich.diaguard.shared.logging

import com.faltenreich.diaguard.shared.di.inject

interface Logger {

    fun verbose(message: String, throwable: Throwable? = null)

    fun debug(message: String, throwable: Throwable? = null)

    fun info(message: String, throwable: Throwable? = null)

    fun warning(message: String, throwable: Throwable? = null)

    fun error(message: String, throwable: Throwable? = null)

    companion object {
        
        private val logger = inject<Logger>()

        fun verbose(message: String, throwable: Throwable? = null) {
            logger.verbose(message, throwable)
        }

        fun debug(message: String, throwable: Throwable? = null) {
            logger.debug(message, throwable)
        }

        fun info(message: String, throwable: Throwable? = null) {
            logger.info(message, throwable)
        }

        fun warning(message: String, throwable: Throwable? = null) {
            logger.warning(message, throwable)
        }

        fun error(message: String, throwable: Throwable? = null) {
            logger.error(message, throwable)
        }
    }
}