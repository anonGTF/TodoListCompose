package com.galih.noteappcompose.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun Date.isTodayPassed(): Boolean {
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)

        val calendarDueDate = Calendar.getInstance()
        calendarDueDate.time = this
        calendarDueDate.set(Calendar.HOUR_OF_DAY, 0)
        calendarDueDate.set(Calendar.MINUTE, 0)
        calendarDueDate.set(Calendar.SECOND, 0)
        calendarDueDate.set(Calendar.MILLISECOND, 0)

        return calendarDueDate.before(currentDate)
    }

    fun LocalDate.toDate(): Date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())


    fun Date.toLocalDate(): LocalDate = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    inline fun <T> Resource<T>.map(
        onSuccess: (Resource.Success<T>) -> Unit,
        onError: (Resource.Error<T>) -> Unit,
        onLoading: (Resource.Loading<T>) -> Unit,
    ) {
        when(this) {
            is Resource.Success -> onSuccess(this)
            is Resource.Error -> onError(this)
            is Resource.Loading -> onLoading(this)
        }
    }

    fun <T> Flow<T>.wrapWithResource(): Flow<Resource<T>> =
        this.map {data ->
            if (data is List<*> && data.isNotEmpty()) {
                Resource.Success(data)
            }
            if (data != null) {
                Resource.Success(data)
            } else {
                Resource.Loading()
            }
        }
            .catch { e -> emit(Resource.Error(e.localizedMessage ?: "Unknown error")) }
            .onStart { emit(Resource.Loading()) } as Flow<Resource<T>>
}