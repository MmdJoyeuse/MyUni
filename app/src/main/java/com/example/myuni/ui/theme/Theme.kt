package com.example.myuni.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = DeepNavy,
    surface = DeepNavy,
    surfaceVariant = DeepNavyLight,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFC4C7C5)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFAFAFC),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF0F2F5),
    onBackground = Color(0xFF111827),
    onSurface = Color(0xFF111827),
    onSurfaceVariant = Color(0xFF4B5563)
)

@Composable
fun MyUniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    customPrimaryColor: Color? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        customPrimaryColor != null -> {
            if (darkTheme) {
                darkColorScheme(
                    primary = customPrimaryColor,
                    secondary = PurpleGrey80,
                    tertiary = Pink80,
                    background = DeepNavy,
                    surface = DeepNavy,
                    surfaceVariant = DeepNavyLight,
                    onBackground = Color.White,
                    onSurface = Color.White,
                    onSurfaceVariant = Color(0xFFC4C7C5)
                )
            } else {
                lightColorScheme(
                    primary = customPrimaryColor,
                    secondary = PurpleGrey40,
                    tertiary = Pink40,
                    background = Color(0xFFFAFAFC),
                    surface = Color(0xFFFFFFFF),
                    surfaceVariant = Color(0xFFF0F2F5),
                    onBackground = Color(0xFF111827),
                    onSurface = Color(0xFF111827),
                    onSurfaceVariant = Color(0xFF4B5563)
                )
            }
        }
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
