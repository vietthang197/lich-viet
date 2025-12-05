package com.auroraarcinteractive.lichviet.data

import java.time.LocalDate
import com.auroraarcinteractive.lichviet.utils.LunarCalendarConverter

/**
 * VietnamHoliday - Data class đại diện cho một ngày lễ Việt Nam
 *
 * @param name Tên ngày lễ
 * @param day Ngày trong tháng (1-31)
 * @param month Tháng trong năm (1-12)
 * @param isLunar Có phải ngày lễ âm lịch không
 * @param isOfficial Có phải ngày lễ chính thức (nghỉ phép) không
 * @param description Mô tả thêm
 */
data class VietnamHoliday(
    val name: String,
    val day: Int,
    val month: Int,
    val isLunar: Boolean,
    val isOfficial: Boolean = true,
    val description: String = ""
)

/**
 * VietnamHolidays - Quản lý danh sách tất cả ngày lễ Việt Nam
 *
 * Dữ liệu từ: Chính phủ Việt Nam, quy định chính thức
 * Cập nhật: 2024-2025
 *
 * NGÀY LỄ DƯƠNG LỊCH (Gregorian):
 * - 1/1: Tết Dương Lịch
 * - 30/4: Ngày Giải Phóng Miền Nam / Ngày Thống Nhất
 * - 1/5: Ngày Quốc Tế Lao Động
 * - 2/9: Quốc Khánh Việt Nam
 *
 * NGÀY LỄ ÂM LỊCH (Lunar):
 * - 1/1 âm: Tết Nguyên Đán (Tết Cổ Truyền)
 * - 10/3 âm: Giỗ Tổ Hùng Vương
 */
object VietnamHolidays {

    /**
     * Danh sách ngày lễ dương lịch (cố định hàng năm)
     */
    val solarHolidays = listOf(
        VietnamHoliday(
            name = "Tết Dương Lịch",
            day = 1,
            month = 1,
            isLunar = false,
            isOfficial = true,
            description = "Ngày đầu năm mới dương lịch - New Year's Day"
        ),
        VietnamHoliday(
            name = "Ngày Giải Phóng Miền Nam",
            day = 30,
            month = 4,
            isLunar = false,
            isOfficial = true,
            description = "Ngày Thống Nhất Đất Nước - Reunification Day"
        ),
        VietnamHoliday(
            name = "Ngày Quốc Tế Lao Động",
            day = 1,
            month = 5,
            isLunar = false,
            isOfficial = true,
            description = "International Labour Day"
        ),
        VietnamHoliday(
            name = "Quốc Khánh",
            day = 2,
            month = 9,
            isLunar = false,
            isOfficial = true,
            description = "Ngày Độc Lập - National Day"
        ),
        VietnamHoliday(
            name = "Giáng Sinh",
            day = 25,
            month = 12,
            isLunar = false,
            isOfficial = false,
            description = "Christmas Day"
        )
    )

    /**
     * Danh sách ngày lễ âm lịch (theo lịch âm, phải tính năm)
     *
     * NGÀY LỄ CHÍNH THỨC (Nghỉ Phép):
     * - Tết Nguyên Đán: Mồng 1 Tết (1/1 âm)
     * - Giỗ Tổ Hùng Vương: 10/3 âm
     */
    val lunarHolidaysBase = listOf(
        VietnamHoliday(
            name = "Tết Nguyên Đán",
            day = 1,
            month = 1,
            isLunar = true,
            isOfficial = true,
            description = "Tết Cổ Truyền Việt Nam - Lunar New Year"
        ),
        VietnamHoliday(
            name = "Mồng 2 Tết",
            day = 2,
            month = 1,
            isLunar = true,
            isOfficial = true,
            description = "Ngày thứ 2 của Tết"
        ),
        VietnamHoliday(
            name = "Mồng 3 Tết",
            day = 3,
            month = 1,
            isLunar = true,
            isOfficial = true,
            description = "Ngày thứ 3 của Tết"
        ),
        VietnamHoliday(
            name = "Mồng 4 Tết",
            day = 4,
            month = 1,
            isLunar = true,
            isOfficial = true,
            description = "Ngày thứ 4 của Tết"
        ),
        VietnamHoliday(
            name = "Mồng 5 Tết",
            day = 5,
            month = 1,
            isLunar = true,
            isOfficial = true,
            description = "Ngày thứ 5 của Tết"
        ),
        VietnamHoliday(
            name = "Giỗ Tổ Hùng Vương",
            day = 10,
            month = 3,
            isLunar = true,
            isOfficial = true,
            description = "Hung Kings' Commemoration Day"
        ),

        // NGÀY LỄ TRUYỀN THỐNG (Không phải ngày nghỉ chính thức)
        VietnamHoliday(
            name = "Tết Nguyên Tiêu",
            day = 15,
            month = 1,
            isLunar = true,
            isOfficial = false,
            description = "Tết Thượng Nguyên"
        ),
        VietnamHoliday(
            name = "Tết Hàn Thực",
            day = 3,
            month = 3,
            isLunar = true,
            isOfficial = false,
            description = "Cold Food Festival"
        ),
        VietnamHoliday(
            name = "Tết Đoan Ngọ",
            day = 5,
            month = 5,
            isLunar = true,
            isOfficial = false,
            description = "Dragon Boat Festival"
        ),
        VietnamHoliday(
            name = "Vu Lan",
            day = 15,
            month = 7,
            isLunar = true,
            isOfficial = false,
            description = "Vu Lan - Ullambana Festival"
        ),
        VietnamHoliday(
            name = "Tết Trung Thu",
            day = 15,
            month = 8,
            isLunar = true,
            isOfficial = false,
            description = "Mid-Autumn Festival"
        ),
        VietnamHoliday(
            name = "Tết Trùng Cửu",
            day = 9,
            month = 9,
            isLunar = true,
            isOfficial = false,
            description = "Double Ninth Day"
        ),
        VietnamHoliday(
            name = "Tết Hạ Nguyên",
            day = 15,
            month = 10,
            isLunar = true,
            isOfficial = false,
            description = "Lower Primordial Festival"
        ),
        VietnamHoliday(
            name = "Ông Táo Chầu Trời",
            day = 23,
            month = 12,
            isLunar = true,
            isOfficial = false,
            description = "Kitchen God Festival"
        )
    )

    /**
     * Lấy danh sách tất cả ngày lễ
     */
    val allHolidays: List<VietnamHoliday>
        get() = solarHolidays + lunarHolidaysBase

    /**
     * Kiểm tra một ngày dương lịch có phải ngày lễ không
     *
     * @param date LocalDate cần kiểm tra
     * @return VietnamHoliday nếu là ngày lễ, null nếu không
     */
    fun getHolidayBySolarDate(date: LocalDate): VietnamHoliday? {
        val day = date.dayOfMonth
        val month = date.monthValue

        return solarHolidays.find {
            it.day == day && it.month == month
        }
    }

    /**
     * Kiểm tra một ngày âm lịch có phải ngày lễ không
     *
     * @param lunarDay Ngày âm lịch
     * @param lunarMonth Tháng âm lịch
     * @param isLeapMonth Có phải tháng nhuận không
     * @return VietnamHoliday nếu là ngày lễ, null nếu không
     */
    fun getHolidayByLunarDate(lunarDay: Int, lunarMonth: Int, isLeapMonth: Boolean = false): VietnamHoliday? {
        // Ngày lễ âm lịch không bao giờ xảy ra vào tháng nhuận
        if (isLeapMonth) return null

        return lunarHolidaysBase.find {
            it.day == lunarDay && it.month == lunarMonth
        }
    }

    /**
     * Lấy danh sách tất cả ngày lễ có thể xảy ra trong một ngày dương lịch
     * (một ngày có thể có cả ngày lễ dương lịch và âm lịch)
     *
     * @param date LocalDate cần kiểm tra
     * @return Danh sách VietnamHoliday
     */
    fun getHolidaysByDate(date: LocalDate): List<VietnamHoliday> {
        val holidays = mutableListOf<VietnamHoliday>()

        // Kiểm tra ngày lễ dương lịch
        getHolidayBySolarDate(date)?.let { holidays.add(it) }

        // Kiểm tra ngày lễ âm lịch
        val lunar = LunarCalendarConverter.solarToLunar(date)
        getHolidayByLunarDate(lunar.day, lunar.month, lunar.isLeapMonth)?.let { holidays.add(it) }

        return holidays
    }

    /**
     * Kiểm tra xem một ngày có phải ngày lễ chính thức (nghỉ phép) không
     *
     * @param date LocalDate cần kiểm tra
     * @return true nếu là ngày lễ chính thức
     */
    fun isOfficialHoliday(date: LocalDate): Boolean {
        return getHolidaysByDate(date).any { it.isOfficial }
    }

    /**
     * Kiểm tra xem một ngày có phải ngày lễ nào đó không (dù chính thức hay truyền thống)
     *
     * @param date LocalDate cần kiểm tra
     * @return true nếu là ngày lễ
     */
    fun isHoliday(date: LocalDate): Boolean {
        return getHolidaysByDate(date).isNotEmpty()
    }

    /**
     * Lấy danh sách ngày lễ trong một tháng/năm
     *
     * @param month Tháng (1-12)
     * @param year Năm
     * @return Danh sách ngày lễ dương lịch trong tháng
     */
    fun getHolidaysInMonth(month: Int, year: Int): List<Pair<LocalDate, VietnamHoliday>> {
        val holidays = mutableListOf<Pair<LocalDate, VietnamHoliday>>()

        // Thêm ngày lễ dương lịch
        solarHolidays.forEach { holiday ->
            if (holiday.month == month) {
                try {
                    val date = LocalDate.of(year, month, holiday.day)
                    holidays.add(date to holiday)
                } catch (e: Exception) {
                    // Invalid date
                }
            }
        }

        // Thêm ngày lễ âm lịch (chuyển sang dương lịch)
        lunarHolidaysBase.forEach { holiday ->
            try {
                val date = LunarCalendarConverter.lunarToSolar(
                    holiday.day,
                    holiday.month,
                    year,
                    false // Không phải tháng nhuận
                )
                if (date.monthValue == month && date.year == year) {
                    holidays.add(date to holiday)
                }
            } catch (e: Exception) {
                // Invalid lunar date
            }
        }

        // Sort by date
        return holidays.sortedBy { it.first }
    }

    /**
     * Lấy danh sách ngày lễ trong một năm
     *
     * @param year Năm
     * @return Danh sách ngày lễ trong năm
     */
    fun getHolidaysInYear(year: Int): List<Pair<LocalDate, VietnamHoliday>> {
        val holidays = mutableListOf<Pair<LocalDate, VietnamHoliday>>()

        for (month in 1..12) {
            holidays.addAll(getHolidaysInMonth(month, year))
        }

        return holidays.sortedBy { it.first }
    }
}
