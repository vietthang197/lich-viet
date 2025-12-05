package com.auroraarcinteractive.lichviet.utils

import com.auroraarcinteractive.lichviet.data.VietnamHoliday
import com.auroraarcinteractive.lichviet.data.VietnamHolidays
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * DateExtensions - Các hàm tiện ích mở rộng cho LocalDate
 *
 * Cung cấp các hàm giúp:
 * - Kiểm tra và lấy thông tin ngày lễ
 * - Format ngày theo định dạng Việt Nam
 * - Kiểm tra các tính chất của ngày (cuối tuần, hôm nay, v.v.)
 */

/**
 * Kiểm tra một ngày có phải là ngày lễ không
 *
 * @return true nếu là ngày lễ (dương hoặc âm), false nếu không
 */
fun LocalDate.isHoliday(): Boolean {
    return VietnamHolidays.isHoliday(this)
}

/**
 * Kiểm tra một ngày có phải ngày lễ chính thức (ngày nghỉ phép) không
 *
 * @return true nếu là ngày lễ chính thức
 */
fun LocalDate.isOfficialHoliday(): Boolean {
    return VietnamHolidays.isOfficialHoliday(this)
}

/**
 * Lấy thông tin ngày lễ (nếu có)
 * Nếu một ngày có nhiều ngày lễ, trả về danh sách
 *
 * @return Danh sách VietnamHoliday hoặc null nếu không phải ngày lễ
 */
fun LocalDate.getHolidays(): List<VietnamHoliday> {
    return VietnamHolidays.getHolidaysByDate(this)
}

/**
 * Lấy tên(s) ngày lễ (nếu có)
 * Nếu có nhiều ngày lễ, danh sách các tên cách nhau bằng ", "
 *
 * @return Tên ngày lễ hoặc null nếu không phải ngày lễ
 */
fun LocalDate.getHolidayName(): String? {
    val holidays = getHolidays()
    return if (holidays.isEmpty()) {
        null
    } else {
        holidays.joinToString(", ") { it.name }
    }
}

/**
 * Lấy danh sách tên tất cả ngày lễ
 *
 * @return Danh sách tên ngày lễ
 */
fun LocalDate.getHolidayNames(): List<String> {
    return getHolidays().map { it.name }
}

/**
 * Kiểm tra có phải cuối tuần không (Thứ 7 hoặc Chủ Nhật)
 */
fun LocalDate.isWeekend(): Boolean {
    val dayOfWeek = this.dayOfWeek.value
    return dayOfWeek == 6 || dayOfWeek == 7 // Saturday or Sunday
}

/**
 * Kiểm tra có phải hôm nay không
 */
fun LocalDate.isToday(): Boolean {
    return this == LocalDate.now()
}

/**
 * Kiểm tra có phải quá khứ không
 */
fun LocalDate.isInPast(): Boolean {
    return this.isBefore(LocalDate.now())
}

/**
 * Kiểm tra có phải tương lai không
 */
fun LocalDate.isInFuture(): Boolean {
    return this.isAfter(LocalDate.now())
}

/**
 * Lấy tên thứ trong tuần bằng tiếng Việt
 *
 * @return "Thứ 2", "Thứ 3", ..., "Chủ Nhật"
 */
fun LocalDate.getVietnameseDayOfWeek(): String {
    return when (this.dayOfWeek.value) {
        1 -> "Thứ 2"
        2 -> "Thứ 3"
        3 -> "Thứ 4"
        4 -> "Thứ 5"
        5 -> "Thứ 6"
        6 -> "Thứ 7"
        7 -> "Chủ Nhật"
        else -> "Không xác định"
    }
}

/**
 * Lấy tên thứ viết tắt (CN, T2, T3, ...)
 */
fun LocalDate.getVietnameseDayOfWeekShort(): String {
    return when (this.dayOfWeek.value) {
        1 -> "T2"
        2 -> "T3"
        3 -> "T4"
        4 -> "T5"
        5 -> "T6"
        6 -> "T7"
        7 -> "CN"
        else -> "??"
    }
}

/**
 * Format ngày dương lịch đầy đủ bằng tiếng Việt
 *
 * Ví dụ: "Chủ Nhật, 06/12/2025"
 *
 * @return Chuỗi định dạng
 */
fun LocalDate.toVietnameseFullString(): String {
    val dayOfWeek = getVietnameseDayOfWeek()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return "$dayOfWeek, ${this.format(formatter)}"
}

/**
 * Format ngày dương lịch đơn giản
 *
 * Ví dụ: "06/12/2025"
 */
fun LocalDate.toVietnameseDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return this.format(formatter)
}

/**
 * Format ngày và tháng
 *
 * Ví dụ: "06/12"
 */
fun LocalDate.toVietnameseDateMonthString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM")
    return this.format(formatter)
}

/**
 * Lấy ngày âm lịch tương ứng
 *
 * @return LunarDate
 */
fun LocalDate.toLunar(): LunarCalendarConverter.LunarDate {
    return LunarCalendarConverter.solarToLunar(this)
}

/**
 * Format ngày âm lịch thành chuỗi tiếng Việt
 *
 * Ví dụ: "Ngày 06 tháng 10 năm 2025"
 */
fun LocalDate.toLunarString(): String {
    val lunar = toLunar()
    return LunarCalendarConverter.formatLunarDate(lunar)
}

/**
 * Format ngày âm lịch gọn gàng
 *
 * Ví dụ: "06/10/2025 âm" hoặc "06 tháng nhuận 10 năm 2025"
 */
fun LocalDate.toLunarShortString(): String {
    val lunar = toLunar()
    val leapStr = if (lunar.isLeapMonth) "nhuận " else ""
    return "Ngày ${lunar.day} tháng $leapStr${lunar.month} năm ${lunar.year}"
}

/**
 * Lấy cả ngày dương lịch và âm lịch
 *
 * Ví dụ: "Thứ 7, 06/12/2025 (Ngày 06 tháng 10 năm 2025)"
 */
fun LocalDate.toFullDateString(): String {
    return "${toVietnameseFullString()}\n(${toLunarShortString()})"
}

/**
 * Lấy tên tháng bằng tiếng Việt
 */
fun LocalDate.getVietnameseMonthName(): String {
    return when (this.monthValue) {
        1 -> "Tháng Giêng"
        2 -> "Tháng Hai"
        3 -> "Tháng Ba"
        4 -> "Tháng Tư"
        5 -> "Tháng Năm"
        6 -> "Tháng Sáu"
        7 -> "Tháng Bảy"
        8 -> "Tháng Tám"
        9 -> "Tháng Chín"
        10 -> "Tháng Mười"
        11 -> "Tháng Mười Một"
        12 -> "Tháng Mười Hai"
        else -> "Không xác định"
    }
}

/**
 * So sánh hai ngày âm lịch
 */
fun LocalDate.compareLunarDate(other: LocalDate): Int {
    val lunar1 = this.toLunar()
    val lunar2 = other.toLunar()

    return when {
        lunar1.year != lunar2.year -> lunar1.year.compareTo(lunar2.year)
        lunar1.month != lunar2.month -> lunar1.month.compareTo(lunar2.month)
        lunar1.day != lunar2.day -> lunar1.day.compareTo(lunar2.day)
        else -> lunar1.isLeapMonth.compareTo(lunar2.isLeapMonth)
    }
}

/**
 * Kiểm tra xem hai ngày có cùng ngày âm lịch không
 */
fun LocalDate.isSameLunarDate(other: LocalDate): Boolean {
    val lunar1 = this.toLunar()
    val lunar2 = other.toLunar()
    return lunar1.day == lunar2.day &&
            lunar1.month == lunar2.month &&
            lunar1.year == lunar2.year &&
            lunar1.isLeapMonth == lunar2.isLeapMonth
}

/**
 * Lấy ngày mồng 1 tháng âm lịch tiếp theo
 */
fun LocalDate.getNextLunarNewMoonDay(): LocalDate {
    val lunar = toLunar()
    val nextLunarMonth = if (lunar.month == 12) 1 else lunar.month + 1
    val nextLunarYear = if (lunar.month == 12) lunar.year + 1 else lunar.year
    return LunarCalendarConverter.lunarToSolar(1, nextLunarMonth, nextLunarYear)
}

/**
 * Lấy ngày mồng 1 tháng âm lịch trước đó
 */
fun LocalDate.getPreviousLunarNewMoonDay(): LocalDate {
    val lunar = toLunar()
    val prevLunarMonth = if (lunar.month == 1) 12 else lunar.month - 1
    val prevLunarYear = if (lunar.month == 1) lunar.year - 1 else lunar.year
    return LunarCalendarConverter.lunarToSolar(1, prevLunarMonth, prevLunarYear)
}

/**
 * Số ngày còn lại đến cuối tháng
 */
fun LocalDate.daysRemainingInMonth(): Int {
    return this.lengthOfMonth() - this.dayOfMonth
}

/**
 * Số ngày từ đầu tháng
 */
fun LocalDate.daysFromStartOfMonth(): Int {
    return this.dayOfMonth
}

/**
 * Độ tuổi theo ngày âm lịch (nếu biết ngày sinh âm lịch)
 *
 * Tính tuổi theo cách truyền thống Việt Nam
 * @param birthDate Ngày sinh
 * @return Tuổi
 */
fun LocalDate.getLunarAge(birthDate: LocalDate): Int {
    val currentLunar = this.toLunar()
    val birthLunar = birthDate.toLunar()
    var age = currentLunar.year - birthLunar.year

    if (currentLunar.month < birthLunar.month ||
        (currentLunar.month == birthLunar.month && currentLunar.day < birthLunar.day)) {
        age--
    }

    return age
}
