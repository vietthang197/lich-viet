package com.auroraarcinteractive.lichviet.data

/**
 * LunarSpecialDays - Đánh dấu các ngày đặc biệt trong âm lịch
 *
 * Ngày đặc biệt:
 * - Mùng 1 âm lịch (Lunar New Month) - Ngày Sơn
 * - Rằm âm lịch (ngày 15) - Ngày Tròn
 *
 * Những ngày này có ý nghĩa tâm linh trong văn hóa Việt
 */
object LunarSpecialDays {

    /**
     * Loại ngày đặc biệt
     */
    enum class LunarSpecialDayType(val displayName: String, val description: String) {
        LUNAR_NEW_MONTH("Mùng 1", "Đầu tháng âm lịch - Ngày Sơn"),
        LUNAR_FULL_MONTH("Rằm", "Rằm tháng - Ngày Tròn (15/tháng âm)")
    }

    /**
     * Data class đại diện cho một ngày đặc biệt âm lịch
     */
    data class LunarSpecialDay(
        val type: LunarSpecialDayType,
        val lunarDay: Int,        // 1 hoặc 15
        val lunarMonth: Int,      // 1-12
        val lunarYear: Int,       // Năm âm
        val displayName: String,
        val description: String,
        val isImportant: Boolean = true
    )

    /**
     * Kiểm tra xem một ngày âm lịch có phải ngày đặc biệt không
     *
     * @param lunarDay Ngày âm (1-30)
     * @param lunarMonth Tháng âm (1-12)
     * @param lunarYear Năm âm
     * @return LunarSpecialDay hoặc null
     */
    fun getSpecialDay(lunarDay: Int, lunarMonth: Int, lunarYear: Int): LunarSpecialDay? {
        return when (lunarDay) {
            1 -> LunarSpecialDay(
                type = LunarSpecialDayType.LUNAR_NEW_MONTH,
                lunarDay = 1,
                lunarMonth = lunarMonth,
                lunarYear = lunarYear,
                displayName = "Mùng 1 tháng $lunarMonth",
                description = "Đầu tháng âm lịch - Ngày Sơn",
                isImportant = true
            )
            15 -> LunarSpecialDay(
                type = LunarSpecialDayType.LUNAR_FULL_MONTH,
                lunarDay = 15,
                lunarMonth = lunarMonth,
                lunarYear = lunarYear,
                displayName = "Rằm tháng $lunarMonth",
                description = "Rằm tháng - Ngày Tròn (15/tháng âm)",
                isImportant = true
            )
            else -> null
        }
    }

    /**
     * Kiểm tra xem có phải mùng 1 không
     *
     * @param lunarDay Ngày âm
     * @return true nếu là mùng 1
     */
    fun isNewLunarMonth(lunarDay: Int): Boolean = lunarDay == 1

    /**
     * Kiểm tra xem có phải rằm không
     *
     * @param lunarDay Ngày âm
     * @return true nếu là ngày 15
     */
    fun isFullLunarMonth(lunarDay: Int): Boolean = lunarDay == 15

    /**
     * Kiểm tra xem ngày đó có phải ngày đặc biệt không
     *
     * @param lunarDay Ngày âm
     * @param lunarMonth Tháng âm
     * @param lunarYear Năm âm
     * @return true nếu là ngày đặc biệt
     */
    fun isSpecialDay(lunarDay: Int, lunarMonth: Int, lunarYear: Int): Boolean {
        return getSpecialDay(lunarDay, lunarMonth, lunarYear) != null
    }

    /**
     * Lấy danh sách tất cả ngày đặc biệt trong một tháng
     *
     * @param lunarMonth Tháng âm (1-12)
     * @param lunarYear Năm âm
     * @return Danh sách ngày đặc biệt (thường là 2 ngày: mùng 1 & rằm)
     */
    fun getSpecialDaysInMonth(lunarMonth: Int, lunarYear: Int): List<LunarSpecialDay> {
        val specialDays = mutableListOf<LunarSpecialDay>()

        // Thêm mùng 1
        getSpecialDay(1, lunarMonth, lunarYear)?.let { specialDays.add(it) }

        // Thêm rằm (ngày 15)
        getSpecialDay(15, lunarMonth, lunarYear)?.let { specialDays.add(it) }

        return specialDays
    }

    /**
     * Lấy thông tin chi tiết của ngày đặc biệt
     *
     * @param type Loại ngày đặc biệt
     * @return Mô tả chi tiết
     */
    fun getDetailedDescription(type: LunarSpecialDayType): String {
        return when (type) {
            LunarSpecialDayType.LUNAR_NEW_MONTH -> """
                |Mùng 1 Tháng Âm Lịch
                |
                |Ý Nghĩa:
                |• Đánh dấu ngày bắt đầu của một tháng mới âm lịch
                |• Được gọi là "Ngày Sơn" trong văn hóa Việt
                |• Là dịp để cầu nguyện, thực hiện các nghi lễ tôn giáo
                |• Phụ nữ thường tham gia các lễ hội tôn giáo vào những ngày này
                |
                |Phong Tục:
                |• Đốt nhang tưởng niệm tổ tiên
                |• Cầu phúc lộc cho gia đình
                |• Tham dự các lễ tế tại các đền chùa
            """.trimMargin()

            LunarSpecialDayType.LUNAR_FULL_MONTH -> """
                |Rằm Tháng Âm Lịch
                |
                |Ý Nghĩa:
                |• Đánh dấu giữa tháng âm lịch
                |• Được gọi là "Ngày Tròn" hoặc "Rằm" trong văn hóa Việt
                |• Mặt trăng tròn sáng nhất trong tháng
                |• Là thời điểm lý tưởng cho các lễ tế và nghi lễ
                |• Có liên quan đến phép tính lịch truyền thống
                |
                |Phong Tục:
                |• Tham gia các lễ tế đầy đủ tại đền chùa
                |• Dâng lễ trái ngon cho tổ tiên
                |• Cầu bình an cho gia đình và những người thân
                |• Là ngày tốt để khởi sự những việc quan trọng
            """.trimMargin()
        }
    }

    /**
     * Lấy icon/ký hiệu cho ngày đặc biệt
     *
     * @param type Loại ngày đặc biệt
     * @return Ký hiệu hiển thị
     */
    fun getSymbol(type: LunarSpecialDayType): String {
        return when (type) {
            LunarSpecialDayType.LUNAR_NEW_MONTH -> "🌑"  // Trăng non - Mùng 1
            LunarSpecialDayType.LUNAR_FULL_MONTH -> "🌕"  // Trăng tròn - Rằm
        }
    }

    /**
     * Lấy màu đặc trưng cho ngày đặc biệt
     *
     * @param type Loại ngày đặc biệt
     * @return Mã màu hex (ví dụ: #FF6B6B)
     */
    fun getColor(type: LunarSpecialDayType): String {
        return when (type) {
            LunarSpecialDayType.LUNAR_NEW_MONTH -> "#6C5CE7"  // Tím - Mùng 1
            LunarSpecialDayType.LUNAR_FULL_MONTH -> "#FFD93D"  // Vàng - Rằm (màu trăng)
        }
    }
}
