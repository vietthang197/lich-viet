package com.auroraarcinteractive.lichviet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.auroraarcinteractive.lichviet.R
import com.auroraarcinteractive.lichviet.data.LunarSpecialDays
import com.auroraarcinteractive.lichviet.ui.theme.HolidayHighlight
import com.auroraarcinteractive.lichviet.ui.theme.LunarText
import com.auroraarcinteractive.lichviet.ui.theme.TodayHighlight
import com.auroraarcinteractive.lichviet.ui.theme.WeekendText
import com.auroraarcinteractive.lichviet.utils.*
import com.auroraarcinteractive.lichviet.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth

/**
 * CalendarScreen - Màn hình chính hiển thị lịch Việt
 *
 * Features:
 * - Lịch dương + âm lịch
 * - Hiển thị ngày lễ chính xác
 * - Đánh dấu Mùng 1 & Rằm âm lịch (chấm đỏ)
 * - Light/Dark theme
 * - Responsive UI
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel()
) {
    val currentYearMonth by viewModel.currentYearMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val days = remember(currentYearMonth) { viewModel.getDaysInMonth() }

    Scaffold(
        topBar = {
            CalendarTopBar(
                yearMonth = currentYearMonth,
                onPreviousMonth = { viewModel.goToPreviousMonth() },
                onNextMonth = { viewModel.goToNextMonth() },
                onToday = { viewModel.goToToday() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {
            // Header thứ trong tuần
            DaysOfWeekHeader()

            Spacer(modifier = Modifier.height(8.dp))

            // Grid lịch
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(days) { date ->
                    DayCell(
                        date = date,
                        isToday = date.isToday(),
                        isInCurrentMonth = viewModel.isDateInCurrentMonth(date),
                        onClick = { viewModel.selectDate(date) }
                    )
                }
            }
        }
    }

    // Bottom sheet cho chi tiết ngày
    selectedDate?.let { date ->
        DayDetailBottomSheet(
            date = date,
            onDismiss = { viewModel.clearSelection() }
        )
    }
}

/**
 * Top bar với navigation và title tháng/năm
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopBar(
    yearMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToday: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = getMonthYearText(yearMonth),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onPreviousMonth) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.prev_month)
                )
            }
        },
        actions = {
            TextButton(onClick = onToday) {
                Text(stringResource(R.string.today))
            }
            IconButton(onClick = onNextMonth) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.next_month)
                )
            }
        }
    )
}

/**
 * Header hiển thị các thứ trong tuần
 */
@Composable
fun DaysOfWeekHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val daysOfWeek = listOf(
            stringResource(R.string.sunday),
            stringResource(R.string.monday),
            stringResource(R.string.tuesday),
            stringResource(R.string.wednesday),
            stringResource(R.string.thursday),
            stringResource(R.string.friday),
            stringResource(R.string.saturday)
        )

        daysOfWeek.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (day == stringResource(R.string.sunday)) {
                    WeekendText
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

/**
 * Ô ngày trong lịch
 *
 * Hiển thị:
 * - Ngày dương (to, rõ)
 * - Ngày âm (nhỏ, dưới)
 * - Đánh dấu ngày hôm nay
 * - Đánh dấu ngày lễ
 * - Đánh dấu Mùng 1 & Rằm âm lịch (chấm đỏ)
 */
@Composable
fun DayCell(
    date: LocalDate,
    isToday: Boolean,
    isInCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    val lunarDate = remember(date) { date.toLunar() }
    val isHoliday = remember(date) { date.isHoliday() }
    val isWeekend = remember(date) { date.isWeekend() }

    // Kiểm tra Mùng 1 & Rằm âm lịch
    val isLunarSpecialDay = remember(lunarDate) {
        LunarSpecialDays.isSpecialDay(lunarDate.day, lunarDate.month, lunarDate.year)
    }

    // Màu nền dựa trên tính chất của ngày
    val backgroundColor = when {
        isToday -> TodayHighlight.copy(alpha = 0.15f)
        isHoliday -> HolidayHighlight.copy(alpha = 0.12f)
        else -> Color.Transparent
    }

    val borderModifier = if (isToday) {
        Modifier.border(
            width = 2.dp,
            color = TodayHighlight,
            shape = RoundedCornerShape(8.dp)
        )
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .then(borderModifier)
            .clickable(onClick = onClick)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Ngày dương
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 13.sp,
                color = when {
                    !isInCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    isToday -> TodayHighlight
                    isHoliday -> HolidayHighlight
                    isWeekend -> WeekendText
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            // Ngày âm (rút gọn)
            val lunarDayText = if (lunarDate.day == 1) {
                "${lunarDate.day}/${lunarDate.month}"
            } else {
                lunarDate.day.toString()
            }

            Text(
                text = lunarDayText,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = if (isInCurrentMonth) {
                    LunarText
                } else {
                    LunarText.copy(alpha = 0.2f)
                }
            )

            // Chấm báo hiệu ngày lễ hoặc Mùng 1 / Rằm
            if ((isHoliday || isLunarSpecialDay) && isInCurrentMonth) {
                Spacer(modifier = Modifier.height(1.dp))
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(HolidayHighlight)  // Màu đỏ
                )
            }
        }
    }
}

/**
 * Helper function - Lấy text tháng/năm
 */
@Composable
fun getMonthYearText(yearMonth: YearMonth): String {
    val monthResId = when (yearMonth.monthValue) {
        1 -> R.string.month_1
        2 -> R.string.month_2
        3 -> R.string.month_3
        4 -> R.string.month_4
        5 -> R.string.month_5
        6 -> R.string.month_6
        7 -> R.string.month_7
        8 -> R.string.month_8
        9 -> R.string.month_9
        10 -> R.string.month_10
        11 -> R.string.month_11
        12 -> R.string.month_12
        else -> R.string.month_1
    }

    return "${stringResource(monthResId)} ${yearMonth.year}"
}
