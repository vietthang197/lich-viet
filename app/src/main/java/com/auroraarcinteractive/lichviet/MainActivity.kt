package com.auroraarcinteractive.lichviet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.auroraarcinteractive.lichviet.ui.CalendarScreen
import com.auroraarcinteractive.lichviet.ui.theme.LichVietTheme

/**
 * MainActivity - Activity chính của ứng dụng Lịch Việt
 *
 * Khởi tạo màn hình lịch với Jetpack Compose và Material 3 theme
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LichVietTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalendarScreen()
                }
            }
        }
    }
}
