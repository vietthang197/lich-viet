package com.auroraarcinteractive.lichviet.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.YearMonth

/**
 * CalendarViewModel - ViewModel quản lý state của màn hình lịch
 *
 * Responsibilities:
 * - Quản lý tháng/năm hiện tại đang hiển thị
 * - Quản lý ngày được chọn
 * - Cung cấp danh sách ngày trong tháng
 * - Xử lý điều hướng tháng trước/sau
 */
class CalendarViewModel : ViewModel() {

    /**
     * State cho tháng/năm hiện tại
     */
    private val _currentYearMonth = MutableStateFlow(YearMonth.now())
    val currentYearMonth: StateFlow<YearMonth> = _currentYearMonth.asStateFlow()

    /**
     * State cho ngày được chọn (để hiển thị detail)
     */
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    /**
     * Ngày hôm nay
     */
    val today: LocalDate = LocalDate.now()

    /**
     * Lấy danh sách tất cả các ngày trong tháng hiện tại
     * Bao gồm cả các ngày của tháng trước/sau để fill grid 7x6
     *
     * @return List<LocalDate> danh sách ngày
     */
    fun getDaysInMonth(): List<LocalDate> {
        val yearMonth = _currentYearMonth.value
        val firstDayOfMonth = yearMonth.atDay(1)
        val lastDayOfMonth = yearMonth.atEndOfMonth()

        // Tìm ngày đầu tiên của tuần chứa ngày 1
        var startDate = firstDayOfMonth
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

        // dayOfWeek.value: Monday=1, Sunday=7
        // Chúng ta muốn: Sunday=0, Monday=1, ..., Saturday=6
        val offset = if (firstDayOfWeek == 7) 0 else firstDayOfWeek
        startDate = startDate.minusDays(offset.toLong())

        // Tạo list 42 ngày (6 tuần x 7 ngày)
        val days = mutableListOf<LocalDate>()
        var currentDate = startDate

        repeat(42) {
            days.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }

        return days
    }

    /**
     * Chuyển sang tháng trước
     */
    fun goToPreviousMonth() {
        _currentYearMonth.value = _currentYearMonth.value.minusMonths(1)
    }

    /**
     * Chuyển sang tháng sau
     */
    fun goToNextMonth() {
        _currentYearMonth.value = _currentYearMonth.value.plusMonths(1)
    }

    /**
     * Quay về tháng hiện tại
     */
    fun goToToday() {
        _currentYearMonth.value = YearMonth.now()
        _selectedDate.value = null
    }

    /**
     * Chọn một ngày để hiển thị detail
     */
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    /**
     * Huỷ chọn ngày (đóng detail)
     */
    fun clearSelection() {
        _selectedDate.value = null
    }

    /**
     * Kiểm tra một ngày có thuộc tháng hiện tại không
     */
    fun isDateInCurrentMonth(date: LocalDate): Boolean {
        val yearMonth = _currentYearMonth.value
        return date.year == yearMonth.year && date.monthValue == yearMonth.monthValue
    }
}
