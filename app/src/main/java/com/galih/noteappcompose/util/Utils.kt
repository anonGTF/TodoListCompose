package com.galih.noteappcompose.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {
    fun String.safeParseDouble(): Double = if (this.isEmpty()) 0.0 else this.toDouble()

    fun Double?.orZero() = this ?: 0.0

    fun Int?.orZero() = this ?: 0

    fun Int?.orOne() = this ?: 1

    fun Int.formatPrice(): String =
        "Rp " + "%,d".format(this).replace(',', '.')

    fun Boolean?.orFalse() = this ?: false

    fun Boolean.let(function: () -> Unit) {
        if (this) function.invoke()
    }

    fun Long?.orZero() = this ?: 0L

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun getTimeMillis() = System.currentTimeMillis()

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun <T> MutableList<T>.getOrDefault(index: Int, default: T): T {
        return try {
            get(index)
        } catch (e: IndexOutOfBoundsException) {
            default
        }
    }
}