package com.auroraarcinteractive.lichviet.data

/**
 * Danh sách đầy đủ các ngày lễ Việt Nam
 *
 * Nguồn:
 * - Ngày lễ dương lịch: Theo pháp luật Việt Nam
 * - Ngày lễ âm lịch: Theo truyền thống
 * - Ngày lễ quốc tế: Theo công nhận quốc tế
 */

enum class HolidayType {
    LUNAR_FESTIVAL,         // Tết nguyên đán, Tết Trung Thu
    VIETNAMESE_HOLIDAY,     // 30/4, 1/9, Tết dương lịch
    INTERNATIONAL_HOLIDAY,  // Giáng Sinh, Valentine
    COMMEMORATION_DAY       // Ngày kỷ niệm (Ngày Anh hùng liệt sỹ)
}

data class Holiday(
    val name: String,
    val month: Int,
    val day: Int,
    val year: Int? = null,  // null = hàng năm
    val isLunar: Boolean,   // true = âm lịch, false = dương lịch
    val type: HolidayType,
    val gregorianDate: Pair<Int, Int>? = null  // (month, day) nếu lịch dương
)

object VietnameseHolidays {

    /**
     * Danh sách ngày lễ DÃY ĐỦ của Việt Nam
     *
     * NGÀY LỄ DƯƠNG LỊCH:
     * 1/1 - Năm mới Dương lịch
     * 30/4 - Ngày Giải phóng miền Nam
     * 1/9 - Ngày Quốc khánh Việt Nam
     * 25/12 - Giáng Sinh
     *
     * NGÀY LỄ ÂM LỊCH:
     * Mùng 1 Tháng 1 - Tết Nguyên Đán (Tết âm lịch)
     * Rằm Tháng 7 - Tết Cô Hàng (Vu Lan Báo Hiếu)
     * Rằm Tháng 8 - Tết Trung Thu
     * Tám Tháng 4 - Tưởng nhớ Phật A Di Đà
     *
     * NGÀY KỶ NIỆM:
     * 27/7 - Ngày Anh hùng liệt sỹ
     */

    // Những ngày lễ chính thức của Việt Nam (dương lịch)
    private val vietnameseHolidaysDuongLich = listOf(
        // Năm mới Dương lịch
        Holiday(
            name = "Năm mới Dương lịch",
            month = 1,
            day = 1,
            isLunar = false,
            type = HolidayType.INTERNATIONAL_HOLIDAY
        ),

        // Ngày Giải phóng miền Nam = Ngày Thống nhất đất nước
        Holiday(
            name = "Ngày Giải phóng miền Nam",
            month = 4,
            day = 30,
            isLunar = false,
            type = HolidayType.VIETNAMESE_HOLIDAY
        ),

        // Ngày Quốc khánh Việt Nam
        Holiday(
            name = "Ngày Quốc khánh Việt Nam",
            month = 9,
            day = 1,
            isLunar = false,
            type = HolidayType.VIETNAMESE_HOLIDAY
        ),

        // Ngày Anh hùng liệt sỹ
        Holiday(
            name = "Ngày Anh hùng liệt sỹ",
            month = 7,
            day = 27,
            isLunar = false,
            type = HolidayType.COMMEMORATION_DAY
        ),

        // Giáng Sinh
        Holiday(
            name = "Giáng Sinh",
            month = 12,
            day = 25,
            isLunar = false,
            type = HolidayType.INTERNATIONAL_HOLIDAY
        )
    )

    // Những ngày lễ âm lịch (luôn luôn)
    private val vietnameseHolidaysAmLich = listOf(
        // Tết Nguyên Đán (Mùng 1 Tháng 1)
        Holiday(
            name = "Tết Nguyên Đán",
            month = 1,
            day = 1,
            isLunar = true,
            type = HolidayType.LUNAR_FESTIVAL
        ),

        // Tết Trung Thu (Rằm Tháng 8)
        Holiday(
            name = "Tết Trung Thu",
            month = 8,
            day = 15,
            isLunar = true,
            type = HolidayType.LUNAR_FESTIVAL
        ),

        // Tết Cô Hàng / Vu Lan Báo Hiếu (Rằm Tháng 7)
        Holiday(
            name = "Vu Lan Báo Hiếu",
            month = 7,
            day = 15,
            isLunar = true,
            type = HolidayType.LUNAR_FESTIVAL
        ),

        // Tưởng nhớ Phật A Di Đà (Tám Tháng 4)
        Holiday(
            name = "Tưởng nhớ Phật A Di Đà",
            month = 4,
            day = 8,
            isLunar = true,
            type = HolidayType.LUNAR_FESTIVAL
        )
    )

    /**
     * Kiểm tra xem ngày (dương lịch) có phải ngày lễ không
     */
    fun isHoliday(month: Int, day: Int): Boolean {
        return vietnameseHolidaysDuongLich.any {
            it.month == month && it.day == day
        }
    }

    /**
     * Kiểm tra xem ngày (âm lịch) có phải ngày lễ âm lịch không
     */
    fun isLunarHoliday(lunarDay: Int, lunarMonth: Int): Boolean {
        return vietnameseHolidaysAmLich.any {
            it.month == lunarMonth && it.day == lunarDay
        }
    }

    /**
     * Lấy tên ngày lễ từ ngày dương lịch
     */
    fun getHolidayName(month: Int, day: Int): String? {
        return vietnameseHolidaysDuongLich.find {
            it.month == month && it.day == day
        }?.name
    }

    /**
     * Lấy tên ngày lễ từ ngày âm lịch
     */
    fun getLunarHolidayName(lunarDay: Int, lunarMonth: Int): String? {
        return vietnameseHolidaysAmLich.find {
            it.month == lunarMonth && it.day == lunarDay
        }?.name
    }

    /**
     * Lấy kiểu ngày lễ
     */
    fun getHolidayType(month: Int, day: Int): HolidayType? {
        return vietnameseHolidaysDuongLich.find {
            it.month == month && it.day == day
        }?.type
    }

    /**
     * Lấy danh sách tất cả ngày lễ dương lịch
     */
    fun getAllDuongLichHolidays(): List<Holiday> = vietnameseHolidaysDuongLich

    /**
     * Lấy danh sách tất cả ngày lễ âm lịch
     */
    fun getAllAmLichHolidays(): List<Holiday> = vietnameseHolidaysAmLich
}
