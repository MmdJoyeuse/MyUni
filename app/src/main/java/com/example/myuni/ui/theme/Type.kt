package com.example.myuni.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.myuni.R

val Vazirmatn = FontFamily(
    Font(R.font.vazirmatn_fd_regular, FontWeight.Normal),
    Font(R.font.vazirmatn_fd_medium, FontWeight.Medium),
    Font(R.font.vazirmatn_fd_bold, FontWeight.Bold)
)

private val base = Typography()

val Typography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = Vazirmatn),
    displayMedium = base.displayMedium.copy(fontFamily = Vazirmatn),
    displaySmall = base.displaySmall.copy(fontFamily = Vazirmatn),
    headlineLarge = base.headlineLarge.copy(fontFamily = Vazirmatn),
    headlineMedium = base.headlineMedium.copy(fontFamily = Vazirmatn),
    headlineSmall = base.headlineSmall.copy(fontFamily = Vazirmatn),
    titleLarge = base.titleLarge.copy(fontFamily = Vazirmatn),
    titleMedium = base.titleMedium.copy(fontFamily = Vazirmatn),
    titleSmall = base.titleSmall.copy(fontFamily = Vazirmatn),
    bodyLarge = base.bodyLarge.copy(fontFamily = Vazirmatn),
    bodyMedium = base.bodyMedium.copy(fontFamily = Vazirmatn),
    bodySmall = base.bodySmall.copy(fontFamily = Vazirmatn),
    labelLarge = base.labelLarge.copy(fontFamily = Vazirmatn),
    labelMedium = base.labelMedium.copy(fontFamily = Vazirmatn),
    labelSmall = base.labelSmall.copy(fontFamily = Vazirmatn)
)
