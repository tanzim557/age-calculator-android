package com.example.agecalculator.viewmodel

import androidx.lifecycle.ViewModel
import com.example.agecalculator.data.AgeCalculatorUiState
import com.example.agecalculator.data.AgeResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.DateTimeException
import java.time.LocalDate
import java.time.Period
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

class AgeCalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AgeCalculatorUiState())
    val uiState: StateFlow<AgeCalculatorUiState> = _uiState.asStateFlow()

    fun showDatePicker() {
        _uiState.update { it.copy(isDatePickerVisible = true, errorMessage = null) }
    }

    fun hideDatePicker() {
        _uiState.update { it.copy(isDatePickerVisible = false) }
    }

    fun onBirthDateSelected(date: LocalDate) {
        val today = _uiState.value.targetDate

        // Validation: Future Date Check
        if (date.isAfter(today)) {
            _uiState.update {
                it.copy(
                    isDatePickerVisible = false,
                    errorMessage = "Date of Birth cannot be in the future!"
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                selectedBirthDate = date,
                isDatePickerVisible = false,
                errorMessage = null
            )
        }
    }

    fun calculateAge() {
        val birthDate = _uiState.value.selectedBirthDate
        val today = _uiState.value.targetDate

        if (birthDate == null) {
            _uiState.update { it.copy(errorMessage = "Please select your Date of Birth first.") }
            return
        }

        if (birthDate.isAfter(today)) {
            _uiState.update { it.copy(errorMessage = "Date of Birth cannot be in the future!") }
            return
        }

        // 1. Exact Period (Years, Months, Days) - Handles leap years accurately
        val period = Period.between(birthDate, today)

        // 2. Absolute Chrono Units
        val totalDays = ChronoUnit.DAYS.between(birthDate, today)
        val totalMonths = ChronoUnit.MONTHS.between(birthDate, today)
        val totalWeeks = totalDays / 7
        val totalHours = totalDays * 24

        // 3. Next Birthday Calculation (Handles Feb 29 leap years)
        var nextBdayYear = today.year
        var nextBirthdayDate = safeLocalDate(nextBdayYear, birthDate.monthValue, birthDate.dayOfMonth)

        val isTodayBirthday = (birthDate.month == today.month && birthDate.dayOfMonth == today.dayOfMonth)

        if (nextBirthdayDate.isBefore(today) || (nextBirthdayDate.isEqual(today) && !isTodayBirthday)) {
            nextBdayYear += 1
            nextBirthdayDate = safeLocalDate(nextBdayYear, birthDate.monthValue, birthDate.dayOfMonth)
        }

        val daysUntilNextBirthday = if (isTodayBirthday) {
            0L
        } else {
            ChronoUnit.DAYS.between(today, nextBirthdayDate)
        }

        val nextBirthdayDayOfWeek = nextBirthdayDate.dayOfWeek.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        )

        val result = AgeResult(
            years = period.years,
            months = period.months,
            days = period.days,
            totalDaysLived = totalDays,
            totalWeeksLived = totalWeeks,
            totalMonthsLived = totalMonths,
            totalHoursLived = totalHours,
            daysUntilNextBirthday = daysUntilNextBirthday,
            nextBirthdayDayOfWeek = nextBirthdayDayOfWeek,
            isBirthdayToday = isTodayBirthday
        )

        _uiState.update { it.copy(result = result, errorMessage = null) }
    }

    fun reset() {
        _uiState.update {
            AgeCalculatorUiState(
                selectedBirthDate = null,
                targetDate = LocalDate.now(),
                result = null,
                errorMessage = null,
                isDatePickerVisible = false
            )
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Safely creates a LocalDate handling Feb 29 for non-leap years (defaults to Feb 28).
     */
    private fun safeLocalDate(year: Int, month: Int, day: Int): LocalDate {
        return try {
            LocalDate.of(year, month, day)
        } catch (e: DateTimeException) {
            if (month == 2 && day == 29) {
                LocalDate.of(year, 2, 28)
            } else {
                LocalDate.of(year, month, 1)
            }
        }
    }
}
