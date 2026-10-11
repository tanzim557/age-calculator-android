package com.example.agecalculator.data

import java.time.LocalDate

/**
 * Detailed calculation results.
 */
data class AgeResult(
    val years: Int,
    val months: Int,
    val days: Int,
    val totalDaysLived: Long,
    val totalWeeksLived: Long,
    val totalMonthsLived: Long,
    val totalHoursLived: Long,
    val daysUntilNextBirthday: Long,
    val nextBirthdayDayOfWeek: String,
    val isBirthdayToday: Boolean
)

/**
 * Single source of truth for the Screen State.
 */
data class AgeCalculatorUiState(
    val selectedBirthDate: LocalDate? = null,
    val targetDate: LocalDate = LocalDate.now(),
    val result: AgeResult? = null,
    val errorMessage: String? = null,
    val isDatePickerVisible: Boolean = false
)
