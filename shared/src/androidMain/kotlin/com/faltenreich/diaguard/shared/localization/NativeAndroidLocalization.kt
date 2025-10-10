package com.faltenreich.diaguard.shared.localization

import android.content.Context
import android.text.format.DateFormat

class NativeAndroidLocalization(private val context: Context) : NativeLocalization {

    override fun is24HourFormat(): Boolean {
        return DateFormat.is24HourFormat(context)
    }
}