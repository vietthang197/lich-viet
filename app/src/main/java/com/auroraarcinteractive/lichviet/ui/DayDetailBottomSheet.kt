package com.auroraarcinteractive.lichviet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auroraarcinteractive.lichviet.R
import com.auroraarcinteractive.lichviet.data.LunarSpecialDays
import com.auroraarcinteractive.lichviet.ui.theme.HolidayHighlight
import com.auroraarcinteractive.lichviet.utils.CanChiHelper
import com.auroraarcinteractive.lichviet.utils.toLunar
import java.time.LocalDate
import java.time.LocalTime

/**
 * DayDetailBottomSheet - Hiển thị chi tiết một ngày
 *
 * Thông tin hiển thị:
 * - Ngày dương lịch + Can Chi năm
 * - Ngày âm lịch + Can Chi ngày
 * - Ngày đặc biệt (Mùng 1 / Rằm)
 * - Can Chi của giờ hiện tại
 * - 12 Giờ hoàng đạo với Can Chi
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailBottomSheet(
    date: LocalDate,
    onDismiss: () -> Unit
) {
    val lunarDate = remember(date) { date.toLunar() }
    val currentTime = remember { LocalTime.now() }

    // Lấy tất cả thông tin Can Chi
    val canChiInfo = remember(date, lunarDate, currentTime) {
        CanChiHelper.getDayCanChiInfo(
            gregorianDate = date,
            lunarDay = lunarDate.day,
            lunarMonth = lunarDate.month,
            lunarYear = lunarDate.year,
            currentHour = currentTime.hour
        )
    }

    // Lấy danh sách 12 giờ hoàng đạo
    val hourList = remember(canChiInfo) {
        CanChiHelper.getAllHourCanChi(
            canChiInfo.dayStemIndex,
            canChiInfo.dayBranchIndex
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // ===== HEADER =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chi tiết ngày",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Divider(
                modifier = Modifier.padding(bottom = 16.dp),
                thickness = 1.dp
            )

            // ===== SECTION: Ngày Dương Lịch =====
            SectionTitle("Ngày Dương Lịch")

            DetailCard(
                label = "Thứ",
                value = date.getVietnameseDayOfWeek()
            )

            DetailCard(
                label = "Ngày",
                value = date.toVietnameseFullString()
            )

            DetailCard(
                label = "Can Chi",
                value = canChiInfo.yearCanChi
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ===== SECTION: Ngày Âm Lịch =====
            SectionTitle("Ngày Âm Lịch")

            DetailCard(
                label = "Ngày âm",
                value = lunarDate.toString()
            )

            DetailCard(
                label = "Can Chi",
                value = canChiInfo.dayCanChi
            )

            // Hiển thị ngày đặc biệt nếu có
            canChiInfo.specialDay?.let { special ->
                Spacer(modifier = Modifier.height(12.dp))
                SpecialDayBadge(
                    symbol = LunarSpecialDays.getSymbol(special.type),
                    name = special.displayName,
                    color = LunarSpecialDays.getColor(special.type)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== SECTION: Can Chi Giờ Hiện Tại =====
            SectionTitle("Can Chi Giờ Hiện Tại")

            if (canChiInfo.currentHourCanChi != null && canChiInfo.currentHourName != null) {
                DetailCard(
                    label = "Giờ",
                    value = canChiInfo.currentHourName ?: "Không xác định"
                )

                DetailCard(
                    label = "Can Chi",
                    value = canChiInfo.currentHourCanChi ?: "Không xác định"
                )
            } else {
                Text(
                    text = "Không có thông tin giờ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== SECTION: Giờ Hoàng Đạo Trong Ngày =====
            SectionTitle("Giờ Hoàng Đạo & Can Chi Trong Ngày")

            Text(
                text = "12 con giáp - Can Chi tương ứng",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Danh sách 12 giờ
            hourList.forEach { hour ->
                LuckyHourItemWithCanChi(
                    hourName = hour.hourName,
                    hourRange = hour.hourRange,
                    canChi = hour.canChi,
                    isCurrentHour = canChiInfo.currentHourName == hour.hourName
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== BUTTON ĐÓNG =====
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(stringResource(R.string.close))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Tiêu đề section
 */
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

/**
 * Card hiển thị thông tin chi tiết (Label - Value)
 */
@Composable
fun DetailCard(
    label: String,
    value: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

/**
 * Badge hiển thị ngày đặc biệt (Mùng 1 / Rằm)
 */
@Composable
fun SpecialDayBadge(
    symbol: String,
    name: String,
    color: String
) {
    val parseColor = try {
        Color(android.graphics.Color.parseColor(color))
    } catch (e: Exception) {
        HolidayHighlight
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = parseColor.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = parseColor.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(symbol, fontSize = 24.sp)
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = parseColor
            )
        }
    }
}

/**
 * Item hiển thị một giờ hoàng đạo với Can Chi
 *
 * Format: "Giờ Tý - Giáp Tý (23h-1h)"
 */
@Composable
fun LuckyHourItemWithCanChi(
    hourName: String,
    hourRange: String,
    canChi: String,
    isCurrentHour: Boolean = false
) {
    val backgroundColor = if (isCurrentHour) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    }

    val borderColor = if (isCurrentHour) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(10.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isCurrentHour) 1.5.dp else 0.5.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "$hourName - $canChi",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isCurrentHour) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isCurrentHour) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = hourRange,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            }

            // Badge "Hiện tại" nếu là giờ hiện tại
            if (isCurrentHour) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Hiện tại",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Extension function - Lấy tên thứ trong tuần bằng Tiếng Việt
 */
fun LocalDate.getVietnameseDayOfWeek(): String {
    return when (this.dayOfWeek.value) {
        1 -> "Thứ 2"
        2 -> "Thứ 3"
        3 -> "Thứ 4"
        4 -> "Thứ 5"
        5 -> "Thứ 6"
        6 -> "Thứ 7"
        7 -> "Chủ nhật"
        else -> "Không xác định"
    }
}

/**
 * Extension function - Lấy chuỗi ngày đầy đủ (dd/MM/yyyy)
 */
fun LocalDate.toVietnameseFullString(): String {
    return "${this.dayOfMonth}/${this.monthValue}/${this.year}"
}
