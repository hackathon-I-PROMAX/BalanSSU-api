package com.yourssu.balanssu.core.utils

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object DDayCalculator {
    fun getDDay(startDate: LocalDate, endDate: LocalDate) = ChronoUnit.DAYS.between(startDate, endDate)
}
