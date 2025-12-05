package com.auroraarcinteractive.lichviet.utils

import java.time.LocalDate

/**
 * CanChiCalculator - Tính toán Can Chi Kinh Điển chính xác
 *
 * Tuân thủ Quy tắc Can Chi Truyền Thống:
 * - Can (Thiên Can): 10 ký (循環 chu kỳ 10 năm)
 *   甲(Giáp), 乙(Ất), 丙(Bính), 丁(Đinh), 戊(Mậu), 己(Kỷ), 庚(Canh), 辛(Tân), 壬(Nhâm), 癸(Quý)
 *
 * - Chi (Địa Chi): 12 ký (循環 chu kỳ 12 năm)
 *   子(Tý), 丑(Sửu), 寅(Dần), 卯(Mão), 辰(Thìn), 巳(Tị), 午(Ngọ), 未(Mùi), 申(Thân), 酉(Dậu), 戌(Tuất), 亥(Hợi)
 *
 * - Can Chi Tuần Hoàn: 60 năm (10 × 6 hoặc 12 × 5)
 *   Giáp Tý (1st) → Quý Hợi (60th) → Giáp Tý (1st again)
 *
 * ĐIỂM MỐC CỐ ĐỊNH (Anchor Point):
 * - Năm Giáp Tý = 1924, 1984, 2044 (60 năm/vòng)
 * - Hoặc: 2020 = Canh Tý
 * - Hoặc: 2025 = Ất Tị
 *
 * TÍNH CAN CỦA NGÀY (Day Stem):
 * - Được tính từ ngày mồng 1 Tết Nguyên Đán của năm đó
 * - Công thức: (Tổng ngày từ điểm mốc + offset) % 10
 *
 * TÍNH CHI CỦA NGÀY (Day Branch):
 * - Được tính dựa trên 12 con giáp tuần hoàn
 * - Công thức: (Tổng ngày từ điểm mốc) % 12
 *
 * TÍNH CAN CHI CỦA GIỜ:
 * - Mỗi ngày bắt đầu với một Can Chi cụ thể
 * - Giờ tiếp theo sử dụng Chi tiếp theo, Can tăng lên (nếu cần)
 * - Ví dụ: Nếu ngày là Giáp Tý, thì:
 *   - Giờ Tý (23h-1h): Giáp Tý
 *   - Giờ Sửu (1h-3h): Ất Sửu
 *   - Giờ Dần (3h-5h): Bính Dần
 *   - ... (Can tăng dần)
 */
object CanChiCalculator {

    // ===== ĐỊNH NGHĨA CAN CHI =====

    /**
     * Danh sách 10 Thiên Can (10-stem cycle)
     * Index: 0=Giáp, 1=Ất, 2=Bính, ..., 9=Quý
     */
    val stems = listOf(
        "Giáp",  // 0 - 甲
        "Ất",    // 1 - 乙
        "Bính",  // 2 - 丙
        "Đinh",  // 3 - 丁
        "Mậu",   // 4 - 戊
        "Kỷ",    // 5 - 己
        "Canh",  // 6 - 庚
        "Tân",   // 7 - 辛
        "Nhâm",  // 8 - 壬
        "Quý"    // 9 - 癸
    )

    /**
     * Danh sách 12 Địa Chi (12-branch cycle)
     * Index: 0=Tý, 1=Sửu, 2=Dần, ..., 11=Hợi
     */
    val branches = listOf(
        "Tý",    // 0 - 子
        "Sửu",   // 1 - 丑
        "Dần",   // 2 - 寅
        "Mão",   // 3 - 卯
        "Thìn",  // 4 - 辰
        "Tị",    // 5 - 巳
        "Ngọ",   // 6 - 午
        "Mùi",   // 7 - 未
        "Thân",  // 8 - 申
        "Dậu",   // 9 - 酉
        "Tuất",  // 10 - 戌
        "Hợi"    // 11 - 亥
    )

    /**
     * Bảng Can Chi Năm từ 1900-2100 (Quy tắc Kinh Điển)
     *
     * Điểm mốc: 1900 = Canh Tý (năm thứ 37 trong chu kỳ 60 năm)
     * Công thức: yearStemIndex = (year - 1900) % 60 / 6
     *           yearBranchIndex = (year - 1900) % 12
     *
     * Nhưng để chính xác, dùng bảng cố định từ 1900 đến 2100
     */
    private val yearCanChiTable = mapOf(
        1900 to Pair(6, 0),   // Canh Tý
        1901 to Pair(7, 1),   // Tân Sửu
        1902 to Pair(8, 2),   // Nhâm Dần
        1903 to Pair(9, 3),   // Quý Mão
        1904 to Pair(0, 4),   // Giáp Thìn
        1905 to Pair(1, 5),   // Ất Tị
        1906 to Pair(2, 6),   // Bính Ngọ
        1907 to Pair(3, 7),   // Đinh Mùi
        1908 to Pair(4, 8),   // Mậu Thân
        1909 to Pair(5, 9),   // Kỷ Dậu
        1910 to Pair(6, 10),  // Canh Tuất
        1911 to Pair(7, 11),  // Tân Hợi
        1912 to Pair(8, 0),   // Nhâm Tý
        // ... (pattern repeats every 60 years)
        2020 to Pair(6, 0),   // Canh Tý
        2021 to Pair(7, 1),   // Tân Sửu
        2022 to Pair(8, 2),   // Nhâm Dần
        2023 to Pair(9, 3),   // Quý Mão
        2024 to Pair(0, 4),   // Giáp Thìn
        2025 to Pair(1, 5),   // Ất Tị
        2026 to Pair(2, 6),   // Bính Ngọ
        2027 to Pair(3, 7),   // Đinh Mùi
        2028 to Pair(4, 8),   // Mậu Thân
        2029 to Pair(5, 9),   // Kỷ Dậu
        2030 to Pair(6, 10),  // Canh Tuất
        2031 to Pair(7, 11),  // Tân Hợi
        2032 to Pair(8, 0),   // Nhâm Tý
        // Tiếp tục quy tắc: (year - 1900) % 60
    )

    /**
     * Lấy Can Chi của năm
     *
     * @param year Năm dương lịch
     * @return Pair(stemIndex, branchIndex)
     */
    fun getYearCanChi(year: Int): Pair<Int, Int> {
        // Nếu có trong bảng, trả về trực tiếp
        if (yearCanChiTable.containsKey(year)) {
            return yearCanChiTable[year]!!
        }

        // Nếu không, tính toán dựa trên quy tắc 60 năm
        val offset = (year - 1900) % 60

        // Can chi của năm cơ sở (1900 = Canh Tý = 6, 0)
        val baseStemIndex = 6  // Canh
        val baseBranchIndex = 0  // Tý

        // Mỗi năm, Can tăng 1, Chi tăng 1 (trong chu kỳ của nó)
        val stemIndex = (baseStemIndex + offset) % 10
        val branchIndex = (baseBranchIndex + offset) % 12

        return Pair(stemIndex, branchIndex)
    }

    /**
     * Lấy tên Can Chi của năm
     *
     * @param year Năm dương lịch
     * @return Chuỗi "Can Chi" ví dụ: "Ất Tị"
     */
    fun getYearCanChiName(year: Int): String {
        val (stemIndex, branchIndex) = getYearCanChi(year)
        return "${stems[stemIndex]} ${branches[branchIndex]}"
    }

    /**
     * Lấy Can Chi của ngày
     *
     * Quy tắc: Tính từ ngày mồng 1 Tết Nguyên Đán (Lunar New Year)
     *
     * @param date Ngày dương lịch
     * @param lunarDate Ngày âm lịch (từ LunarCalendarConverter)
     * @return Pair(stemIndex, branchIndex)
     */
    fun getDayCanChi(date: LocalDate, lunarDay: Int, lunarMonth: Int, lunarYear: Int): Pair<Int, Int> {
        // Điểm mốc: Mồng 1 Tết Nguyên Đán năm 1900 = Giáp Ngự (4, 6)
        // Nhưng để chính xác, ta cần tính từ ngày trong năm âm lịch

        // Công thức đơn giản (Quy tắc Kinh Điển):
        // Số ngày từ điểm mốc cố định → chia cho 10 (can) và 12 (chi)

        val baseDate = LocalDate.of(1900, 1, 31)  // Mồng 1 Tết Nguyên Đán 1900 (approximate)
        val daysSinceBase = date.toEpochDay() - baseDate.toEpochDay()

        // Điểm mốc: Mồng 1 Tết 1900 = Giáp Ngự (4, 6)
        val baseStemIndex = 4
        val baseBranchIndex = 6

        val stemIndex = ((baseStemIndex + daysSinceBase.toInt()) % 10 + 10) % 10
        val branchIndex = ((baseBranchIndex + daysSinceBase.toInt()) % 12 + 12) % 12

        return Pair(stemIndex, branchIndex)
    }

    /**
     * Lấy tên Can Chi của ngày
     *
     * @param date Ngày dương lịch
     * @param lunarDay Ngày âm
     * @param lunarMonth Tháng âm
     * @param lunarYear Năm âm
     * @return Chuỗi "Can Chi" ví dụ: "Giáp Ngư"
     */
    fun getDayCanChiName(date: LocalDate, lunarDay: Int, lunarMonth: Int, lunarYear: Int): String {
        val (stemIndex, branchIndex) = getDayCanChi(date, lunarDay, lunarMonth, lunarYear)
        return "${stems[stemIndex]} ${branches[branchIndex]}"
    }

    /**
     * Lấy Can Chi của giờ
     *
     * Quy tắc: Trong một ngày có Can Chi nhất định, từng giờ (2h = 1 con giáp) có chi riêng
     *
     * Ví dụ: Nếu ngày = Giáp Tý (stemIndex=0, branchIndex=0)
     * - Giờ Tý (23h-1h): Giáp Tý (0, 0)
     * - Giờ Sửu (1h-3h): Ất Sửu (1, 1)
     * - Giờ Dần (3h-5h): Bính Dần (2, 2)
     * - v.v...
     *
     * @param dayStemIndex Can của ngày
     * @param dayBranchIndex Chi của ngày
     * @param hour Giờ (0-23)
     * @return Pair(stemIndex, branchIndex)
     */
    fun getHourCanChi(dayStemIndex: Int, dayBranchIndex: Int, hour: Int): Pair<Int, Int> {
        // Chuyển giờ sang chỉ số con giáp (0-11)
        // Giờ Tý (23h-1h): 0
        // Giờ Sửu (1h-3h): 1
        // ...
        val hourBranchIndex = when {
            hour in 23..23 || hour in 0..0 -> 0  // Tý
            hour in 1..2 -> 1                     // Sửu
            hour in 3..4 -> 2                     // Dần
            hour in 5..6 -> 3                     // Mão
            hour in 7..8 -> 4                     // Thìn
            hour in 9..10 -> 5                    // Tị
            hour in 11..12 -> 6                   // Ngọ
            hour in 13..14 -> 7                   // Mùi
            hour in 15..16 -> 8                   // Thân
            hour in 17..18 -> 9                   // Dậu
            hour in 19..20 -> 10                  // Tuất
            hour in 21..22 -> 11                  // Hợi
            else -> 0
        }

        // Can của giờ = (Can của ngày + chỉ số Con giáp) % 10
        val hourStemIndex = (dayStemIndex + hourBranchIndex) % 10
        val hourBranchIndexFinal = (dayBranchIndex + hourBranchIndex) % 12

        return Pair(hourStemIndex, hourBranchIndexFinal)
    }

    /**
     * Lấy tên Can Chi của giờ
     *
     * @param dayStemIndex Can của ngày
     * @param dayBranchIndex Chi của ngày
     * @param hour Giờ (0-23)
     * @return Chuỗi "Can Chi" ví dụ: "Giáp Tý"
     */
    fun getHourCanChiName(dayStemIndex: Int, dayBranchIndex: Int, hour: Int): String {
        val (stemIndex, branchIndex) = getHourCanChi(dayStemIndex, dayBranchIndex, hour)
        return "${stems[stemIndex]} ${branches[branchIndex]}"
    }

    /**
     * Lấy tên Can (Thiên Can) từ index
     */
    fun getStemName(index: Int): String = stems[index % 10]

    /**
     * Lấy tên Chi (Địa Chi) từ index
     */
    fun getBranchName(index: Int): String = branches[index % 12]
}
