package com.example.myuni.supabase

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.myuni.AppConfig

@Composable
fun AppUpdateDialog(
    config: AppConfigRemote,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AlertDialog(
            onDismissRequest = {
                if (!config.forceUpdate) {
                    onDismissRequest()
                }
            },
            shape = RoundedCornerShape(AppConfig.dialogCornerRadius),
            tonalElevation = AppConfig.dialogTonalElevation,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppConfig.dialogButtonSpacing)
                ) {
                    Icon(
                        imageVector = Icons.Default.SystemUpdate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "نسخه جدید موجود است! (${config.latestVersionName})",
                        fontWeight = AppConfig.dialogTitleFontWeight,
                        fontSize = AppConfig.dialogTitleFontSize
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppConfig.dialogButtonSpacing)
                ) {
                    if (!config.releaseNotes.isNullOrBlank()) {
                        Text(
                            text = "تغییرات نسخه جدید:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = AppConfig.dialogSubtitleFontSize,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = AppConfig.dialogCardContainerAlpha),
                            shape = RoundedCornerShape(AppConfig.dialogInnerCornerRadius),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = config.releaseNotes ?: "",
                                fontSize = AppConfig.dialogBodyFontSize,
                                modifier = Modifier.padding(10.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Text(
                            text = "نسخه جدید برنامه آماده دانلود می‌باشد.",
                            fontSize = AppConfig.dialogSubtitleFontSize,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        AppUpdateHelper.openMarket(context, config.downloadUrl)
                        onDismissRequest()
                    },
                    shape = RoundedCornerShape(AppConfig.dialogButtonCornerRadius)
                ) {
                    Text("به‌روزرسانی از بازار", fontSize = AppConfig.dialogButtonFontSize)
                }
            },
            dismissButton = {
                if (!config.forceUpdate) {
                    TextButton(onClick = onDismissRequest) {
                        Text("بعداً", fontSize = AppConfig.dialogButtonFontSize)
                    }
                }
            }
        )
    }
}
