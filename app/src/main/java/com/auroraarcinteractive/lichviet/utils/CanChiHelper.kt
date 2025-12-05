package com.auroraarcinteractive.lichviet.utils

import com.auroraarcinteractive.lichviet.data.LunarSpecialDays
import java.time.LocalDate

/**
 * CanChiHelper - Công cụ hỗ trợ sử dụng Can Chi & Ngày Đặc Biệt
 *
 * Tích hợp CanChiCalculator + LunarSpecialDays để dễ sử dụng trong UI
 */
object CanChiHelper {

    /**
     * Data class chứa tất cả thông tin Can Chi của một ngày
     */
    data class DayCanChiInfo(
        val yearCanChi: String,              // Ví dụ: "Ất Tị"
        val dayCanChi: String,               // Ví dụ: "Giáp Ngư"
        val dayStemIndex: Int,               // 0-9
        val dayBranchIndex: Int,             // 0-11
        val currentHourCanChi: String?,      // Ví dụ: "Canh Tị" (null nếu giờ không hợp lệ)
        val currentHourName: String?,        // Ví dụ: "Giờ Tị (9h-11h)"
        val specialDay: LunarSpecialDays.LunarSpecialDay?  // null nếu không phải ngày đặc biệt
    )

    /**
     * Lấy tất cả thông tin Can Chi của một ngày
     *
     * @param gregorianDate Ngày dương lịch
     * @param lunarDay Ngày âm lịch
     * @param lunarMonth Tháng âm lịch
     * @param lunarYear Năm âm lịch
     * @param currentHour Giờ hiện tại (nếu cần) - mặc định là -1 (không tính)
     * @return DayCanChiInfo chứa tất cả thông tin
     */
    fun getDayCanChiInfo(
        gregorianDate: LocalDate,
        lunarDay: Int,
        lunarMonth: Int,
        lunarYear: Int,
        currentHour: Int = -1
    ): DayCanChiInfo {
        // Lấy Can Chi của năm
        val yearCanChi = CanChiCalculator.getYearCanChiName(gregorianDate.year)

        // Lấy Can Chi của ngày
        val dayCanChi = CanChiCalculator.getDayCanChiName(gregorianDate, lunarDay, lunarMonth, lunarYear)
        val (dayStemIndex, dayBranchIndex) = CanChiCalculator.getDayCanChi(
            gregorianDate, lunarDay, lunarMonth, lunarYear
        )

        // Lấy Can Chi của giờ (nếu có)
        var currentHourCanChi: String? = null
        var currentHourName: String? = null

        if (currentHour in 0..23) {
            currentHourCanChi = CanChiCalculator.getHourCanChiName(dayStemIndex, dayBranchIndex, currentHour)
            currentHourName = getHourName(currentHour)
        }

        // Kiểm tra ngày đặc biệt
        val specialDay = LunarSpecialDays.getSpecialDay(lunarDay, lunarMonth, lunarYear)

        return DayCanChiInfo(
            yearCanChi = yearCanChi,
            dayCanChi = dayCanChi,
            dayStemIndex = dayStemIndex,
            dayBranchIndex = dayBranchIndex,
            currentHourCanChi = currentHourCanChi,
            currentHourName = currentHourName,
            specialDay = specialDay
        )
    }

    /**
     * Lấy danh sách tất cả Can Chi giờ trong ngày
     *
     * @param dayStemIndex Can của ngày (0-9)
     * @param dayBranchIndex Chi của ngày (0-11)
     * @return Danh sách 12 Can Chi giờ
     */
    fun getAllHourCanChi(dayStemIndex: Int, dayBranchIndex: Int): List<HourCanChiInfo> {
        return (0..23).map { hour ->
            val canChi = CanChiCalculator.getHourCanChiName(dayStemIndex, dayBranchIndex, hour)
            val hourName = getHourName(hour)
            val hourRange = getHourRange(hour)

            HourCanChiInfo(
                hour = hour,
                hourName = hourName,
                hourRange = hourRange,
                canChi = canChi
            )
        }.distinctBy { it.hourName }  // Loại bỏ trùng lặp (vì 2 giờ = 1 con giáp)
    }

    /**
     * Data class chứa thông tin Can Chi của một giờ
     */
    data class HourCanChiInfo(
        val hour: Int,           // 0-23
        val hourName: String,    // Ví dụ: "Giờ Tý"
        val hourRange: String,   // Ví dụ: "23h-1h"
        val canChi: String       // Ví dụ: "Giáp Tý"
    )

    /**
     * Lấy tên giờ con giáp từ giờ (0-23)
     *
     * @param hour Giờ (0-23)
     * @return Tên giờ con giáp, ví dụ: "Giờ Tý"
     */
    fun getHourName(hour: Int): String {
        return when (hour) {
            23, 0 -> "Giờ Tý"
            1, 2 -> "Giờ Sửu"
            3, 4 -> "Giờ Dần"
            5, 6 -> "Giờ Mão"
            7, 8 -> "Giờ Thìn"
            9, 10 -> "Giờ Tị"
            11, 12 -> "Giờ Ngọ"
            13, 14 -> "Giờ Mùi"
            15, 16 -> "Giờ Thân"
            17, 18 -> "Giờ Dậu"
            19, 20 -> "Giờ Tuất"
            21, 22 -> "Giờ Hợi"
            else -> "Không xác định"
        }
    }

    /**
     * Lấy khoảng giờ từ giờ (0-23)
     *
     * @param hour Giờ (0-23)
     * @return Khoảng giờ, ví dụ: "23h-1h"
     */
    fun getHourRange(hour: Int): String {
        return when (hour) {
            23 -> "23h-1h"
            0 -> "23h-1h"
            1, 2 -> "1h-3h"
            3, 4 -> "3h-5h"
            5, 6 -> "5h-7h"
            7, 8 -> "7h-9h"
            9, 10 -> "9h-11h"
            11, 12 -> "11h-13h"
            13, 14 -> "13h-15h"
            15, 16 -> "15h-17h"
            17, 18 -> "17h-19h"
            19, 20 -> "19h-21h"
            21, 22 -> "21h-23h"
            else -> "Không xác định"
        }
    }

    /**
     * Kiểm tra xem ngày có phải Mùng 1 không
     */
    fun isNewLunarMonth(lunarDay: Int): Boolean {
        return LunarSpecialDays.isNewLunarMonth(lunarDay)
    }

    /**
     * Kiểm tra xem ngày có phải Rằm không
     */
    fun isFullLunarMonth(lunarDay: Int): Boolean {
        return LunarSpecialDays.isFullLunarMonth(lunarDay)
    }

    /**
     * Kiểm tra xem ngày có phải ngày đặc biệt không
     */
    fun isSpecialDay(lunarDay: Int, lunarMonth: Int, lunarYear: Int): Boolean {
        return LunarSpecialDays.isSpecialDay(lunarDay, lunarMonth, lunarYear)
    }

    /**
     * Lấy chi tiết Can Chi năm dạng String
     *
     * @param year Năm dương lịch
     * @return String mô tả, ví dụ: "Năm Ất Tị (Tị = Hợi)"
     */
    fun getYearCanChiDescription(year: Int): String {
        val canChi = CanChiCalculator.getYearCanChiName(year)
        val (stemIdx, branchIdx) = CanChiCalculator.getYearCanChi(year)
        val branchName = CanChiCalculator.branches[branchIdx]
        val animalName = getZodiacAnimal(branchIdx)

        return "Năm $canChi ($branchName = $animalName)"
    }

    /**
     * Lấy tên con giáp từ chi index
     */
    fun getZodiacAnimal(branchIndex: Int): String {
        return when (branchIndex) {
            0 -> "Chuột"
            1 -> "Trâu"
            2 -> "Hổ"
            3 -> "Mèo"
            4 -> "Rồng"
            5 -> "Rắn"
            6 -> "Ngựa"
            7 -> "Dê"
            8 -> "Khỉ"
            9 -> "Gà"
            10 -> "Chó"
            11 -> "Lợn"
            else -> "Không xác định"
        }
    }
}
