package com.galih.noteappcompose.util

import java.util.Calendar
import java.util.Date
import java.util.UUID
import kotlin.random.Random

object Faker {
    fun aString(include: String = ""): String = UUID.randomUUID().toString() + include

    fun anInt(from: Int = 1, to: Int = 99999): Int = (from..to).random()

    fun aDouble(): Double = Random.nextDouble(1.0, 10.0)

    fun <T> aList(randomElm: () -> T): List<T> {
        val temp: MutableList<T> = mutableListOf()
        for (i in 1..20) {
            temp.add(randomElm())
        }
        return temp
    }

    fun aDate(startYear: Int = 2023, endYear: Int = 2024): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, startYear)
        val startMillis = calendar.timeInMillis

        calendar.set(Calendar.YEAR, endYear)
        val endMillis = calendar.timeInMillis

        val diff = endMillis - startMillis + 1
        val randomMillis = startMillis + Random.nextLong() % diff

        return Date(randomMillis)
    }

    fun aBoolean() = Random.nextBoolean()
}