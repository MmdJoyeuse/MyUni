package com.example.myuni

import com.example.myuni.R
import com.example.myuni.supabase.AppConfigRemote
import com.example.myuni.supabase.AppUpdateDialog
import com.example.myuni.supabase.SupabaseRepository
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import java.util.Calendar
import java.util.Locale
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.key
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.AbsoluteAlignment
import kotlin.math.roundToInt
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.myuni.ui.theme.MyUniTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Fullscreen immersive sticky mode
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        setContent {
            val context = LocalContext.current
            val prefs = remember { context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE) }
            val rawColorInt = prefs.getInt("themeColor", Color(0xFF2196F3).toArgb())
            val savedColorInt = if (rawColorInt == Color(0xFFFC0FC0).toArgb()) {
                val softPink = Color(0xFFEC407A).toArgb()
                prefs.edit().putInt("themeColor", softPink).apply()
                softPink
            } else {
                rawColorInt
            }
            var selectedThemeColor by remember { mutableStateOf(Color(savedColorInt)) }
            var darkModeMode by remember {
                mutableStateOf(
                    prefs.getString("darkModeMode", null) ?: if (prefs.getBoolean("isDarkMode", false)) "dark" else "light"
                )
            }

            val systemIsDark = isSystemInDarkTheme()
            val isDarkMode = when (darkModeMode) {
                "dark" -> true
                "system" -> systemIsDark
                else -> false
            }

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                MyUniTheme(customPrimaryColor = selectedThemeColor, darkTheme = isDarkMode) {
                    MainScreen(
                        selectedColor = selectedThemeColor,
                        onColorChange = { newColor ->
                            selectedThemeColor = newColor
                            prefs.edit().putInt("themeColor", newColor.toArgb()).apply()
                        },
                        darkModeMode = darkModeMode,
                        onDarkModeModeChange = { newMode ->
                            darkModeMode = newMode
                            prefs.edit().putString("darkModeMode", newMode).apply()
                        }
                    )
                }
            }
        }
    }
}

private fun getSlotKey(timeLabel: String, mode: String): String {
    return if (mode == "golestan") {
        timeLabel.substringBefore(":")
    } else {
        timeLabel
    }
}

private fun getSlotStartMinutes(timeLabel: String): Int {
    val parts = timeLabel.split(":")
    val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
    return h * 60 + m
}

private fun getSlotEndMinutes(timeLabel: String, mode: String): Int {
    val startMin = getSlotStartMinutes(timeLabel)
    return if (mode.startsWith("holding")) {
        when (timeLabel) {
            "8:30" -> 10 * 60
            "10:15" -> 11 * 60 + 45
            "12:30" -> 14 * 60
            "14:15" -> 15 * 60 + 45
            "16:00" -> 17 * 60 + 30
            else -> startMin + 90
        }
    } else {
        startMin + 60
    }
}

private fun getSlotEndLabel(timeLabel: String, mode: String): String {
    if (mode.startsWith("holding")) {
        return when (timeLabel) {
            "8:30" -> "10:00"
            "10:15" -> "11:45"
            "12:30" -> "14:00"
            "14:15" -> "15:45"
            "16:00" -> "17:30"
            else -> {
                val endMin = getSlotEndMinutes(timeLabel, mode)
                val endH = endMin / 60
                val endM = endMin % 60
                if (endM == 0) "$endH:00" else "$endH:$endM"
            }
        }
    } else {
        val endMin = getSlotEndMinutes(timeLabel, mode)
        val endH = endMin / 60
        val endM = endMin % 60
        return if (endM == 0) "$endH:00" else "$endH:$endM"
    }
}

private fun isEvenWeekContinuous(
    timeInMillis: Long = System.currentTimeMillis(),
    isWeekFlipped: Boolean = false
): Boolean {
    val tehranZone = java.util.TimeZone.getTimeZone("Asia/Tehran")
    // Anchor Saturday: January 6, 2024 00:00:00 Tehran Time
    val anchorSaturdayMillis = 1704486600000L
    
    val offset = tehranZone.getOffset(timeInMillis)
    val localTimeMillis = timeInMillis + offset
    val anchorLocalTimeMillis = anchorSaturdayMillis + tehranZone.getOffset(anchorSaturdayMillis)
    
    val diffMillis = localTimeMillis - anchorLocalTimeMillis
    val diffDays = diffMillis / (24 * 60 * 60 * 1000L)
    
    val weekIndex = if (diffDays >= 0) diffDays / 7 else (diffDays - 6) / 7
    val isEven = (weekIndex % 2L == 0L)
    return if (isWeekFlipped) !isEven else isEven
}

private fun getHoldingTableData(
    rawTableData: Map<String, String>,
    isEvenWeek: Boolean?,
    visibleDays: List<String>
): Map<String, String> {
    val converted = mutableMapOf<String, String>()
    
    visibleDays.forEach { day ->
        val golestanHours = listOf("8", "9", "10", "11", "12", "13", "14", "15", "16", "17")
        
        golestanHours.forEach { hStr ->
            val h = hStr.toIntOrNull() ?: return@forEach
            val blockKey = "block_${day}_$hStr"
            val roomKey = "room_${day}_$hStr"
            val durationKey = "duration_${day}_$hStr"
            
            val text = rawTableData[blockKey]
            val room = rawTableData[roomKey] ?: ""
            val durationStr = rawTableData[durationKey] ?: "1"
            
            if (text.isNullOrBlank() && room.isBlank()) return@forEach
            
            val duration = durationStr.toIntOrNull() ?: 1
            
            val showInThisWeek: Boolean
            val targetGolestanHour: Int
            
            if (duration >= 2) {
                showInThisWeek = true
                targetGolestanHour = if (h % 2 == 0) h else h - 1
            } else {
                if (isEvenWeek == null) {
                    showInThisWeek = true
                    targetGolestanHour = if (h % 2 == 0) h else h - 1
                } else if (h % 2 == 0) {
                    showInThisWeek = !isEvenWeek
                    targetGolestanHour = h
                } else {
                    showInThisWeek = isEvenWeek
                    targetGolestanHour = h - 1
                }
            }
            
            if (showInThisWeek) {
                val holdingSlotKey = when (targetGolestanHour) {
                    8 -> "8:30"
                    10 -> "10:15"
                    12 -> "12:30"
                    14 -> "14:15"
                    16 -> "16:00"
                    else -> "$targetGolestanHour:30"
                }
                
                val targetBlockKey = "block_${day}_$holdingSlotKey"
                val targetRoomKey = "room_${day}_$holdingSlotKey"
                val targetDurationKey = "duration_${day}_$holdingSlotKey"
                
                if (converted.containsKey(targetBlockKey) && isEvenWeek == null) {
                    val existingText = converted[targetBlockKey] ?: ""
                    val existingRoom = converted[targetRoomKey] ?: ""
                    
                    if (existingText.isNotBlank() && !text.isNullOrBlank() && text != existingText && !existingText.contains(text)) {
                        converted[targetBlockKey] = "$existingText\n$text"
                    } else if (existingText.isBlank() && !text.isNullOrBlank()) {
                        converted[targetBlockKey] = text
                    }
                    
                    if (existingRoom.isNotBlank() && room.isNotBlank() && room != existingRoom && !existingRoom.contains(room)) {
                        converted[targetRoomKey] = "$existingRoom / $room"
                    } else if (existingRoom.isBlank() && room.isNotBlank()) {
                        converted[targetRoomKey] = room
                    }
                } else {
                    converted[targetBlockKey] = text ?: ""
                    converted[targetRoomKey] = room
                    converted[targetDurationKey] = "1"
                }
            }
        }

        // Direct holding keys fallback
        listOf("8:30", "10:15", "12:30", "14:15", "16:00").forEach { slotKey ->
            val targetBlockKey = "block_${day}_$slotKey"
            val targetRoomKey = "room_${day}_$slotKey"
            val targetDurationKey = "duration_${day}_$slotKey"

            if (!converted.containsKey(targetBlockKey) && rawTableData.containsKey(targetBlockKey)) {
                val directText = rawTableData[targetBlockKey]
                if (!directText.isNullOrBlank()) {
                    converted[targetBlockKey] = directText
                    converted[targetRoomKey] = rawTableData[targetRoomKey] ?: ""
                    converted[targetDurationKey] = rawTableData[targetDurationKey] ?: "1"
                }
            }
        }
    }
    
    return converted
}

private fun handleBatchTableDataChange(
    changes: List<Pair<String, String>>,
    currentScheduleMode: String,
    tableData: MutableMap<String, String>,
    prefs: SharedPreferences
) {
    val editor = prefs.edit()

    for ((rawKey, value) in changes) {
        val parts = rawKey.split("_")
        if (parts.size >= 3) {
            val type = parts[0] // "block", "room", "duration"
            val day = parts[1]
            val slot = parts.subList(2, parts.size).joinToString("_")

            if (slot.contains(":")) {
                // Holding slot (e.g. "8:30", "10:15", "12:30", "14:15", "16:00")
                val baseHour = when (slot) {
                    "8:30" -> 8
                    "10:15" -> 10
                    "12:30" -> 12
                    "14:15" -> 14
                    "16:00" -> 16
                    else -> slot.substringBefore(":").toIntOrNull() ?: 8
                }

                val golestanHour = when (currentScheduleMode) {
                    "holding_even" -> baseHour + 1
                    "holding_odd" -> baseHour
                    else -> baseHour
                }

                val mappedKey = "${type}_${day}_$golestanHour"
                val durationMappedKey = "duration_${day}_$golestanHour"
                val durationRawKey = "duration_${day}_$slot"

                if (value.isBlank()) {
                    val h1 = baseHour
                    val h2 = baseHour + 1

                    val keysToRemove = listOf(
                        "block_${day}_$h1", "room_${day}_$h1", "duration_${day}_$h1",
                        "block_${day}_$h2", "room_${day}_$h2", "duration_${day}_$h2",
                        "block_${day}_$slot", "room_${day}_$slot", "duration_${day}_$slot"
                    )

                    for (k in keysToRemove) {
                        tableData.remove(k)
                        editor.remove(k)
                    }
                } else {
                    val finalValue = if (type == "duration" && currentScheduleMode.startsWith("holding")) "2" else value
                    tableData[mappedKey] = finalValue
                    tableData[rawKey] = finalValue
                    editor.putString(mappedKey, finalValue)
                    editor.putString(rawKey, finalValue)

                    if (type == "block") {
                        val defaultDuration = if (currentScheduleMode.startsWith("holding")) "2" else "1"
                        if (!tableData.containsKey(durationMappedKey) || tableData[durationMappedKey].isNullOrBlank()) {
                            tableData[durationMappedKey] = defaultDuration
                            tableData[durationRawKey] = defaultDuration
                            editor.putString(durationMappedKey, defaultDuration)
                            editor.putString(durationRawKey, defaultDuration)
                        }
                    }
                }
            } else {
                if (value.isBlank()) {
                    tableData.remove(rawKey)
                    editor.remove(rawKey)
                } else {
                    tableData[rawKey] = value
                    editor.putString(rawKey, value)
                }
            }
        } else {
            if (value.isBlank()) {
                tableData.remove(rawKey)
                editor.remove(rawKey)
            } else {
                tableData[rawKey] = value
                editor.putString(rawKey, value)
            }
        }
    }

    editor.apply()
}

private fun handleTableDataChange(
    rawKey: String,
    value: String,
    currentScheduleMode: String,
    tableData: MutableMap<String, String>,
    prefs: SharedPreferences
) {
    handleBatchTableDataChange(listOf(rawKey to value), currentScheduleMode, tableData, prefs)
}

data class DraggedCourseInfo(
    val sourceDay: String,
    val sourceSlotKey: String,
    val title: String,
    val room: String,
    val duration: String
)

private fun findCourseHeadKey(
    day: String,
    slotKey: String,
    hourLabels: List<String>,
    tableData: Map<String, String>,
    scheduleMode: String
): String {
    if (!tableData["block_${day}_$slotKey"].isNullOrEmpty()) {
        return slotKey
    }
    val targetIndex = hourLabels.indexOfFirst { getSlotKey(it, scheduleMode) == slotKey }
    if (targetIndex > 0) {
        for (prevIndex in (targetIndex - 1) downTo 0) {
            val prevSlotKey = getSlotKey(hourLabels[prevIndex], scheduleMode)
            val prevDuration = tableData["duration_${day}_$prevSlotKey"]?.toIntOrNull() ?: 1
            val prevBlock = tableData["block_${day}_$prevSlotKey"]
            if (!prevBlock.isNullOrEmpty() && prevIndex + prevDuration > targetIndex) {
                return prevSlotKey
            }
        }
    }
    return slotKey
}

private fun performCourseSwapOrMove(
    dragged: DraggedCourseInfo,
    targetDay: String,
    targetSlotKey: String,
    tableData: Map<String, String>,
    onBatchValueChange: (List<Pair<String, String>>) -> Unit
) {
    val sourceDay = dragged.sourceDay
    val sourceSlotKey = dragged.sourceSlotKey

    val sourceBlockKey = "block_${sourceDay}_$sourceSlotKey"
    val sourceRoomKey = "room_${sourceDay}_$sourceSlotKey"
    val sourceDurationKey = "duration_${sourceDay}_$sourceSlotKey"

    val targetBlockKey = "block_${targetDay}_$targetSlotKey"
    val targetRoomKey = "room_${targetDay}_$targetSlotKey"
    val targetDurationKey = "duration_${targetDay}_$targetSlotKey"

    val targetTitle = tableData[targetBlockKey] ?: ""
    val targetRoom = tableData[targetRoomKey] ?: ""
    val targetDuration = tableData[targetDurationKey] ?: "1"

    val changes = mutableListOf<Pair<String, String>>()

    // Target receives source course
    changes.add(targetBlockKey to dragged.title)
    changes.add(targetRoomKey to dragged.room)
    changes.add(targetDurationKey to dragged.duration)

    // Source receives target course (or cleared if target was empty)
    if (targetTitle.isNotBlank()) {
        changes.add(sourceBlockKey to targetTitle)
        changes.add(sourceRoomKey to targetRoom)
        changes.add(sourceDurationKey to targetDuration)
    } else {
        changes.add(sourceBlockKey to "")
        changes.add(sourceRoomKey to "")
        changes.add(sourceDurationKey to "")
    }

    onBatchValueChange(changes)
}

@Composable
fun ScheduleTable(
    tableData: Map<String, String>,
    selectedColor: Color,
    visibleDays: List<String>,
    hourLabels: List<String>,
    scheduleMode: String = "golestan",
    isEditingMode: Boolean = (scheduleMode == "golestan"),
    isOverallMode: Boolean = (scheduleMode == "golestan_all"),
    isWeekFlipped: Boolean,
    onValueChange: (String, String) -> Unit,
    onBatchValueChange: (List<Pair<String, String>>) -> Unit = { changes ->
        changes.forEach { onValueChange(it.first, it.second) }
    },
    onAddDeadline: (String) -> Unit = {},
    onTableSettingsRequest: () -> Unit = {},
    onEditActiveDaysClick: () -> Unit = {},
    onEditActiveHoursClick: () -> Unit = {},
    onConfirmEditClick: () -> Unit = {},
    onReturnToEvenOddMode: () -> Unit = {}
) {
    val context = LocalContext.current
    var clickedCourseForOptions by remember { mutableStateOf<String?>(null) }

    if (clickedCourseForOptions != null) {
        val courseTitle = clickedCourseForOptions!!
        RtlAlertDialog(
            onDismissRequest = { clickedCourseForOptions = null },
            title = {
                Text(
                    text = "درس: $courseTitle",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = selectedColor
                )
            },
            text = {
                Text("آیا می‌خواهید برای این درس ددلاین جدیدی ایجاد کنید؟", fontSize = 13.sp)
            },
            confirmButton = {
                Button(
                    onClick = {
                        clickedCourseForOptions = null
                        onAddDeadline(courseTitle)
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("افزودن ددلاین", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { clickedCourseForOptions = null }) {
                    Text("لغو")
                }
            }
        )
    }

    val lineThickness = AppConfig.lineThickness
    val lineAlpha = AppConfig.lineAlpha
    val vLineThickness = AppConfig.vLineThickness
    val vLineAlpha = AppConfig.vLineAlpha
    val blockFontSize = AppConfig.blockFontSize

    var editingBlockKey by remember { mutableStateOf<String?>(null) }
    var selectingDurationKey by remember { mutableStateOf<String?>(null) }
    var editingText by remember { mutableStateOf("") }
    var editingRoomText by remember { mutableStateOf("") }
    var editingTextValue by remember { mutableStateOf(TextFieldValue("")) }
    var editingRoomTextValue by remember { mutableStateOf(TextFieldValue("")) }
    var isEditingRoom by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Drag and Drop state
    var draggedCourse by remember { mutableStateOf<DraggedCourseInfo?>(null) }
    var dragPositionInWindow by remember { mutableStateOf(Offset.Zero) }
    var targetCell by remember { mutableStateOf<Pair<String, String>?>(null) }

    val cellBounds = remember { mutableStateMapOf<Pair<String, String>, Rect>() }
    val cellRects = remember { mutableStateMapOf<Pair<String, String>, Rect>() }
    var rootCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var tableRootBounds by remember { mutableStateOf<Rect?>(null) }
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current

    // Real-time tracking
    var currentTime by remember {
        mutableStateOf(Calendar.getInstance().apply { firstDayOfWeek = Calendar.SATURDAY })
    }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance().apply { firstDayOfWeek = Calendar.SATURDAY }
            delay(30000L) // Refresh every 30 seconds
        }
    }

    val isCurrentWeekEven = isEvenWeekContinuous(currentTime.timeInMillis, isWeekFlipped)

    val currentDayPersian = when (currentTime.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SATURDAY -> "شنبه"
        Calendar.SUNDAY -> "یکشنبه"
        Calendar.MONDAY -> "دوشنبه"
        Calendar.TUESDAY -> "سه‌شنبه"
        Calendar.WEDNESDAY -> "چهارشنبه"
        Calendar.THURSDAY -> "پنجشنبه"
        Calendar.FRIDAY -> "جمعه"
        else -> ""
    }
    val nowH = currentTime.get(Calendar.HOUR_OF_DAY)
    val nowM = currentTime.get(Calendar.MINUTE)

    // Blinking Animation for current time line and active block
    val infiniteTransition = rememberInfiniteTransition(label = "timeBlinker")
    val blinkingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blinkingAlpha"
    )

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .onGloballyPositioned { coords ->
                    tableRootBounds = coords.boundsInWindow()
                    rootCoordinates = coords
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // 1. Upper Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onTableSettingsRequest() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1.3f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isEditingMode) {
                            Surface(
                                onClick = { onConfirmEditClick() },
                                shape = RoundedCornerShape(8.dp),
                                color = selectedColor,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "تایید",
                                        textAlign = TextAlign.Center,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        } else if (isOverallMode) {
                            Surface(
                                onClick = { onReturnToEvenOddMode() },
                                shape = RoundedCornerShape(8.dp),
                                color = selectedColor,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "برگشت",
                                        textAlign = TextAlign.Center,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        } else {
                            val weekType = if (isCurrentWeekEven) "هفته زوج" else "هفته فرد"
                            Text(
                                text = weekType,
                                textAlign = TextAlign.Center,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = selectedColor
                            )
                        }
                    }
                    VerticalDivider(thickness = vLineThickness, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = vLineAlpha))

                    visibleDays.forEachIndexed { index, day ->
                        val isToday = day == currentDayPersian

                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day,
                                textAlign = TextAlign.Center,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isToday) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        VerticalDivider(thickness = vLineThickness, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = vLineAlpha))
                    }
                }
                HorizontalDivider(thickness = lineThickness, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = lineAlpha))

                // 2. Main Content Rows
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Time labels column
                        Column(
                            modifier = Modifier
                                .weight(1.3f)
                                .fillMaxHeight()
                        ) {
                            val firstHourOffset = AppConfig.firstHourOffset
                            val middleHourOffset = AppConfig.middleHourOffset
                            val lastHourOffset = AppConfig.lastHourOffset
                            
                            hourLabels.forEachIndexed { index, timeLabel ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val isFirst = index == 0
                                    val isLast = index == hourLabels.size - 1
                                    val isHolding = scheduleMode.startsWith("holding")
                                    
                                    if (isHolding) {
                                        val endLabel = getSlotEndLabel(timeLabel, scheduleMode)
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = timeLabel,
                                                fontSize = AppConfig.timeLabelFontSize,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center,
                                                softWrap = false
                                            )
                                            Text(
                                                text = "تا",
                                                fontSize = AppConfig.timeTillFontSize,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                                textAlign = TextAlign.Center,
                                                softWrap = false
                                            )
                                            Text(
                                                text = endLabel,
                                                fontSize = AppConfig.timeLabelFontSize,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center,
                                                softWrap = false
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = timeLabel,
                                            fontSize = AppConfig.timeLabelFontSize,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .align(Alignment.TopCenter)
                                                .offset(y = if (isFirst) firstHourOffset else middleHourOffset)
                                                .zIndex(1f),
                                            softWrap = false
                                        )

                                        if (isLast) {
                                            val endLabel = getSlotEndLabel(timeLabel, scheduleMode)

                                            Text(
                                                text = endLabel,
                                                fontSize = AppConfig.timeLabelFontSize,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .align(Alignment.BottomCenter)
                                                    .offset(y = lastHourOffset)
                                                    .zIndex(1f),
                                                softWrap = false
                                            )
                                        }
                                    }


                                }
                                HorizontalDivider(thickness = lineThickness, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = lineAlpha))
                            }
                        }
                        VerticalDivider(thickness = vLineThickness, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = vLineAlpha))

                        // Days Columns
                        visibleDays.forEach { day ->
                            val isToday = day == currentDayPersian

                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .fillMaxHeight()
                            ) {
                                var skipRowCounter = 0
                                hourLabels.forEachIndexed { index, timeLabel ->
                                    if (skipRowCounter > 0) { skipRowCounter--; return@forEachIndexed }

                                    val slotKey = getSlotKey(timeLabel, scheduleMode)
                                    val key = "block_${day}_$slotKey"
                                    val roomKey = "room_${day}_$slotKey"
                                    val durationKey = "duration_${day}_$slotKey"

                                    val text = tableData[key]
                                    val roomText = tableData[roomKey]
                                    val duration = tableData[durationKey]?.toIntOrNull() ?: 1
                                    val isEditing = editingBlockKey == key

                                    val blockStartMin = getSlotStartMinutes(timeLabel)
                                    val endSlotIndex = (index + duration - 1).coerceAtMost(hourLabels.size - 1)
                                    val blockEndMin = getSlotEndMinutes(hourLabels[endSlotIndex], scheduleMode)

                                    val nowTotalMin = nowH * 60 + nowM
                                    val isActiveBlock = isToday && (nowTotalMin >= blockStartMin && nowTotalMin < blockEndMin)

                                    val isEvenSlot = index % 2 == 0
                                    val isNeutralizedByWeek = if (isOverallMode || scheduleMode.startsWith("holding")) false else duration == 1 && (
                                        (isEvenSlot && isCurrentWeekEven) || (!isEvenSlot && !isCurrentWeekEven)
                                    )

                                    val hasContent = !text.isNullOrEmpty() || !roomText.isNullOrEmpty()
                                    val isBeingDragged = draggedCourse?.sourceDay == day && draggedCourse?.sourceSlotKey == slotKey

                                    if (hasContent || isEditing) {
                                        val cardWeight = duration.coerceAtMost(hourLabels.size - index)
                                        skipRowCounter = cardWeight - 1

                                        val blockColor = when {
                                            isBeingDragged -> selectedColor.copy(alpha = 0.35f)
                                            isActiveBlock -> selectedColor.copy(alpha = blinkingAlpha)
                                            isNeutralizedByWeek && !isEditing && !isEditingMode -> Color.Gray.copy(alpha = 0.3f)
                                            else -> selectedColor
                                        }

                                        val canDrag = !isEditing && scheduleMode == "golestan"

                                        val dragModifier = if (canDrag) {
                                            Modifier.pointerInput(key, text, roomText, duration) {
                                                detectDragGesturesAfterLongPress(
                                                    onDragStart = { localOffset ->
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        val cardRect = cellBounds[day to slotKey]
                                                        val cardTopLeft = cardRect?.topLeft ?: Offset.Zero
                                                        dragPositionInWindow = cardTopLeft + localOffset
                                                        draggedCourse = DraggedCourseInfo(
                                                            sourceDay = day,
                                                            sourceSlotKey = slotKey,
                                                            title = text ?: "",
                                                            room = roomText ?: "",
                                                            duration = duration.toString()
                                                        )
                                                        targetCell = day to slotKey
                                                    },
                                                    onDrag = { change, dragAmount ->
                                                        change.consume()
                                                        dragPositionInWindow += dragAmount

                                                        val hoverCell = cellBounds.entries.minByOrNull { (_, rect) ->
                                                            val dx = dragPositionInWindow.x - rect.center.x
                                                            val dy = dragPositionInWindow.y - rect.center.y
                                                            dx * dx + dy * dy
                                                        }?.key

                                                        if (hoverCell != null) {
                                                            val resolvedSlotKey = findCourseHeadKey(
                                                                hoverCell.first,
                                                                hoverCell.second,
                                                                hourLabels,
                                                                tableData,
                                                                scheduleMode
                                                            )
                                                            targetCell = hoverCell.first to resolvedSlotKey
                                                        }
                                                    },
                                                    onDragEnd = {
                                                        val currentDragged = draggedCourse
                                                        val currentTarget = targetCell
                                                        if (currentDragged != null && currentTarget != null) {
                                                            val (tDay, tSlot) = currentTarget
                                                            if (tDay != currentDragged.sourceDay || tSlot != currentDragged.sourceSlotKey) {
                                                                performCourseSwapOrMove(
                                                                    dragged = currentDragged,
                                                                    targetDay = tDay,
                                                                    targetSlotKey = tSlot,
                                                                    tableData = tableData,
                                                                    onBatchValueChange = onBatchValueChange
                                                                )
                                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            }
                                                        }
                                                        draggedCourse = null
                                                        targetCell = null
                                                    },
                                                    onDragCancel = {
                                                        draggedCourse = null
                                                        targetCell = null
                                                    }
                                                )
                                            }
                                        } else Modifier

                                        Box(
                                            modifier = Modifier
                                                .weight(cardWeight.toFloat())
                                                .fillMaxWidth()
                                                .padding(2.dp)
                                                .onGloballyPositioned { coords ->
                                                    val cardWindowRect = coords.boundsInWindow()
                                                    cellBounds[day to slotKey] = cardWindowRect

                                                    val root = rootCoordinates
                                                    if (root != null) {
                                                        val pos = root.localPositionOf(coords, Offset.Zero)
                                                        val cardRect = Rect(pos.x, pos.y, pos.x + coords.size.width, pos.y + coords.size.height)
                                                        cellRects[day to slotKey] = cardRect

                                                        if (cardWeight > 1) {
                                                            val singleHeight = cardRect.height / cardWeight
                                                            val singleWindowHeight = cardWindowRect.height / cardWeight
                                                            for (step in 1 until cardWeight) {
                                                                val subIndex = index + step
                                                                if (subIndex < hourLabels.size) {
                                                                    val subSlotKey = getSlotKey(hourLabels[subIndex], scheduleMode)
                                                                    cellBounds[day to subSlotKey] = Rect(
                                                                        left = cardWindowRect.left,
                                                                        top = cardWindowRect.top + step * singleWindowHeight,
                                                                        right = cardWindowRect.right,
                                                                        bottom = cardWindowRect.top + (step + 1) * singleWindowHeight
                                                                    )
                                                                    cellRects[day to subSlotKey] = Rect(
                                                                        left = cardRect.left,
                                                                        top = cardRect.top + step * singleHeight,
                                                                        right = cardRect.right,
                                                                        bottom = cardRect.top + (step + 1) * singleHeight
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(blockColor)
                                                .then(
                                                    if (isBeingDragged) {
                                                        Modifier.border(2.0.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                                    } else if (isActiveBlock) {
                                                        Modifier.border(1.5.dp, Color.White.copy(alpha = blinkingAlpha), RoundedCornerShape(6.dp))
                                                    } else Modifier
                                                )
                                                .then(dragModifier)
                                                .then(
                                                    Modifier.clickable {
                                                        if (!isBeingDragged) {
                                                            val courseTitle = text?.lines()?.firstOrNull()?.trim() ?: ""
                                                            if (courseTitle.isNotBlank()) {
                                                                clickedCourseForOptions = courseTitle
                                                            } else if (scheduleMode == "golestan") {
                                                                editingBlockKey = key
                                                                val currentText = text ?: ""
                                                                editingText = currentText
                                                                editingTextValue = TextFieldValue(text = currentText, selection = TextRange(currentText.length))
                                                                val currentRoomText = tableData[roomKey] ?: ""
                                                                editingRoomText = currentRoomText
                                                                editingRoomTextValue = TextFieldValue(text = currentRoomText, selection = TextRange(currentRoomText.length))
                                                                isEditingRoom = false
                                                                selectingDurationKey = null
                                                            }
                                                        }
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier
                                                    .padding(2.dp)
                                                    .padding(bottom = if (isEditing || isEditingMode) 14.dp else 0.dp)
                                            ) {
                                                if (isEditing) {
                                                    if (!isEditingRoom) {
                                                        BasicTextField(
                                                            value = editingTextValue,
                                                            onValueChange = { newValue ->
                                                                editingTextValue = newValue
                                                                editingText = newValue.text
                                                                onValueChange(key, newValue.text)
                                                            },
                                                            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                                                            textStyle = TextStyle(color = Color.White, fontSize = blockFontSize, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
                                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                                            keyboardActions = KeyboardActions(onNext = {
                                                                isEditingRoom = true
                                                                val currentRoomText = tableData[roomKey] ?: editingRoomText
                                                                editingRoomTextValue = TextFieldValue(text = currentRoomText, selection = TextRange(currentRoomText.length))
                                                            }),
                                                            decorationBox = { innerTextField ->
                                                                if (editingTextValue.text.isEmpty()) Text("نام درس", color = Color.White.copy(alpha = 0.5f), fontSize = (blockFontSize.value - 1).sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                                                innerTextField()
                                                            }
                                                        )
                                                    } else {
                                                        BasicTextField(
                                                            value = editingRoomTextValue,
                                                            onValueChange = { newValue ->
                                                                editingRoomTextValue = newValue
                                                                editingRoomText = newValue.text
                                                                onValueChange(roomKey, newValue.text)
                                                            },
                                                            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                                                            textStyle = TextStyle(color = Color.White, fontSize = blockFontSize, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
                                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                                            keyboardActions = KeyboardActions(onDone = {
                                                                if (editingTextValue.text.isBlank() && editingRoomTextValue.text.isBlank()) {
                                                                    onValueChange(key, "")
                                                                    onValueChange(roomKey, "")
                                                                    onValueChange(durationKey, "")
                                                                }
                                                                editingBlockKey = null
                                                                focusManager.clearFocus()
                                                            }),
                                                            decorationBox = { innerTextField ->
                                                                if (editingRoomTextValue.text.isEmpty()) Text("شماره کلاس", color = Color.White.copy(alpha = 0.5f), fontSize = (blockFontSize.value - 1).sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                                                innerTextField()
                                                            }
                                                        )
                                                    }
                                                    LaunchedEffect(isEditingRoom) {
                                                        if (!isEditingRoom) {
                                                            val currentText = tableData[key] ?: editingText
                                                            editingTextValue = TextFieldValue(text = currentText, selection = TextRange(currentText.length))
                                                        } else {
                                                            val currentRoomText = tableData[roomKey] ?: editingRoomText
                                                            editingRoomTextValue = TextFieldValue(text = currentRoomText, selection = TextRange(currentRoomText.length))
                                                        }
                                                        focusRequester.requestFocus()
                                                    }
                                                } else {
                                                    if (!text.isNullOrEmpty()) {
                                                        Text(text = text, color = Color.White, fontSize = blockFontSize, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, maxLines = 3)
                                                    }
                                                    if (!roomText.isNullOrEmpty()) {
                                                        Text(text = roomText, color = Color.White.copy(alpha = 0.9f), fontSize = (blockFontSize.value - 2).sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Normal)
                                                    }
                                                }
                                            }

                                            if (isEditing || isEditingMode) {
                                                Box(
                                                    modifier = Modifier
                                                        .align(Alignment.BottomCenter)
                                                        .fillMaxWidth()
                                                        .height(14.dp)
                                                        .background(Color.Black.copy(alpha = 0.2f))
                                                        .clickable {
                                                            onValueChange(key, "")
                                                            onValueChange(roomKey, "")
                                                            onValueChange(durationKey, "")
                                                            if (editingBlockKey == key) {
                                                                editingBlockKey = null
                                                            }
                                                            focusManager.clearFocus()
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        Icons.Default.Delete,
                                                        contentDescription = "حذف",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(10.dp)
                                                    )
                                                }
                                            }
                                        }
                                        HorizontalDivider(thickness = lineThickness, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = lineAlpha))
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                                .onGloballyPositioned { coords ->
                                                    cellBounds[day to slotKey] = coords.boundsInWindow()
                                                    val root = rootCoordinates
                                                    if (root != null) {
                                                        val pos = root.localPositionOf(coords, Offset.Zero)
                                                        cellRects[day to slotKey] = Rect(pos.x, pos.y, pos.x + coords.size.width, pos.y + coords.size.height)
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (selectingDurationKey == key) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(2.dp)
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(selectedColor),
                                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier.weight(1f).fillMaxHeight().clickable { 
                                                            onValueChange(durationKey, "1")
                                                            onValueChange(key, "")
                                                            onValueChange(roomKey, "")
                                                            editingBlockKey = key
                                                            editingText = ""
                                                            editingTextValue = TextFieldValue("", selection = TextRange(0))
                                                            editingRoomText = ""
                                                            editingRoomTextValue = TextFieldValue("", selection = TextRange(0))
                                                            isEditingRoom = false
                                                            selectingDurationKey = null
                                                        },
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text("1h", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                                                    }
                                                    Box(modifier = Modifier.width(1.dp).fillMaxHeight(0.6f).background(Color.White.copy(alpha = 0.4f)))
                                                    Box(
                                                        modifier = Modifier.weight(1f).fillMaxHeight().clickable { 
                                                            onValueChange(durationKey, "2")
                                                            onValueChange(key, "")
                                                            onValueChange(roomKey, "")
                                                            editingBlockKey = key
                                                            editingText = ""
                                                            editingTextValue = TextFieldValue("", selection = TextRange(0))
                                                            editingRoomText = ""
                                                            editingRoomTextValue = TextFieldValue("", selection = TextRange(0))
                                                            isEditingRoom = false
                                                            selectingDurationKey = null
                                                        },
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text("2h", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                                                    }
                                                }
                                            } else {
                                                Box(
                                                    modifier = Modifier.fillMaxSize()
                                                        .clickable {
                                                            if (scheduleMode == "golestan") {
                                                                if (index == hourLabels.size - 1) {
                                                                    onValueChange(durationKey, "1")
                                                                    onValueChange(key, "")
                                                                    onValueChange(roomKey, "")
                                                                    editingBlockKey = key
                                                                    editingText = ""
                                                                    editingTextValue = TextFieldValue("", selection = TextRange(0))
                                                                    editingRoomText = ""
                                                                    editingRoomTextValue = TextFieldValue("", selection = TextRange(0))
                                                                    isEditingRoom = false
                                                                    selectingDurationKey = null
                                                                } else {
                                                                    selectingDurationKey = key
                                                                    editingBlockKey = null
                                                                }
                                                            } else {
                                                                onTableSettingsRequest()
                                                            }
                                                        }
                                                )
                                            }
                                        }
                                        HorizontalDivider(thickness = lineThickness, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = lineAlpha))
                                    }
                                }
                            }
                            VerticalDivider(thickness = vLineThickness, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = vLineAlpha))
                        }
                    }
                }
            }


        }
    }
}

data class WebSite(val name: String, val url: String, val id: Long? = null)

data class TelegramChannel(val name: String, val id: String, val url: String, val eitaaUrl: String = "", val remoteId: Long? = null)

data class ContactInfo(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,        // سمت شخص
    val name: String,         // فامیل/نام شخص
    val phone: String = "",   // شماره تماس
    val telegram: String = "",// آیدی تلگرام
    val eitaa: String = "",   // آیدی ایتا
    val email: String = ""    // ایمیل
)

data class ContactDetailDialogState(
    val contact: ContactInfo,
    val selectedType: String? = null,
    val selectedValue: String? = null,
    val openUrl: String? = null
)

data class DeadlineItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val courseName: String,
    val category: String, // "homework", "midterm", "final"
    val dueDate: String = "",
    val dueTime: String = "23:00",
    val dueTimestamp: Long = 0L,
    val hasNotification: Boolean = false,
    val description: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

fun parseDueTimestamp(dateStr: String, timeStr: String): Long {
    try {
        val cleanDate = dateStr.trim().replace('-', '/')
        val parts = cleanDate.split('/')
        if (parts.size == 3) {
            val y = parts[0].toIntOrNull() ?: return 0L
            val m = parts[1].toIntOrNull() ?: 1
            val d = parts[2].toIntOrNull() ?: 1

            val primaryTime = if (timeStr.contains("-") || timeStr.contains("تا") || timeStr.contains("الی") || timeStr.contains("الى")) {
                timeStr.split(Regex("""\s*(?:-|تا|الی|الى|_|/|~|to)\s*""")).firstOrNull() ?: timeStr
            } else {
                timeStr
            }

            val timeParts = primaryTime.trim().split(':')
            val h = if (timeParts.size >= 1) timeParts[0].toIntOrNull() ?: 12 else 12
            val min = if (timeParts.size >= 2) timeParts[1].toIntOrNull() ?: 0 else 0

            val (gYear, gMonth, gDay) = if (y in 1300..1500) {
                JalaliCalendarHelper.jalaliToGregorian(y, m, d)
            } else {
                Triple(y, m, d)
            }

            val cal = Calendar.getInstance().apply {
                set(gYear, gMonth - 1, gDay, h, min, 0)
                set(Calendar.MILLISECOND, 0)
            }
            return cal.timeInMillis
        }
    } catch (_: Exception) {}
    return 0L
}

fun scheduleDeadlineAlarm(context: Context, item: DeadlineItem) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, DeadlineAlarmReceiver::class.java).apply {
        putExtra("deadline_id", item.id)
        putExtra("deadline_title", item.title)
    }
    val requestCode = item.id.hashCode()
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    if (!item.hasNotification || item.isCompleted || item.dueTimestamp <= System.currentTimeMillis()) {
        alarmManager.cancel(pendingIntent)
        return
    }

    setAlarmAndAllowWhileIdle(alarmManager, item.dueTimestamp, pendingIntent)
}

class DeadlineAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("deadline_title") ?: "ددلاین"
        val channelId = "deadline_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "یادآوری ددلاین‌ها",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "اعلان‌های مربوط به رسیدن موعد ددلاین‌ها"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("یادآوری موعد ددلاین")
            .setContentText("موعد ددلاین «$title» فرا رسیده است!")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(title.hashCode(), builder.build())
    }
}

fun loadDeadlines(context: Context): List<DeadlineItem> {
    val prefs = context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE)
    val jsonString = prefs.getString("saved_deadlines_v1", null) ?: return emptyList()
    return try {
        val array = org.json.JSONArray(jsonString)
        val list = mutableListOf<DeadlineItem>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val dDate = obj.optString("dueDate", "")
            val dTime = obj.optString("dueTime", "23:00")
            val rawTs = obj.optLong("dueTimestamp", 0L)
            val ts = if (rawTs != 0L) rawTs else parseDueTimestamp(dDate, dTime)

            list.add(
                DeadlineItem(
                    id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                    title = obj.optString("title", ""),
                    courseName = obj.optString("courseName", "آزاد"),
                    category = obj.optString("category", "homework"),
                    dueDate = dDate,
                    dueTime = dTime,
                    dueTimestamp = ts,
                    hasNotification = obj.optBoolean("hasNotification", false),
                    description = obj.optString("description", ""),
                    isCompleted = obj.optBoolean("isCompleted", false),
                    createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                )
            )
        }
        list
    } catch (e: Exception) {
        emptyList()
    }
}

fun saveDeadlines(context: Context, items: List<DeadlineItem>) {
    val prefs = context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE)
    val array = org.json.JSONArray()
    for (item in items) {
        val obj = org.json.JSONObject()
        obj.put("id", item.id)
        obj.put("title", item.title)
        obj.put("courseName", item.courseName)
        obj.put("category", item.category)
        obj.put("dueDate", item.dueDate)
        obj.put("dueTime", item.dueTime)
        obj.put("dueTimestamp", item.dueTimestamp)
        obj.put("hasNotification", item.hasNotification)
        obj.put("description", item.description)
        obj.put("isCompleted", item.isCompleted)
        obj.put("createdAt", item.createdAt)
        array.put(obj)
    }
    prefs.edit().putString("saved_deadlines_v1", array.toString()).apply()
}

@Composable
fun ScrollableListWithThumb(
    selectedColor: Color,
    modifier: Modifier = Modifier,
    itemCount: Int = 0,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .then(if (itemCount > 7) Modifier.verticalScroll(scrollState) else Modifier),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }

        if (itemCount > 7 && scrollState.maxValue > 0) {
            BoxWithConstraints(
                modifier = Modifier
                    .width(10.dp)
                    .fillMaxHeight()
                    .padding(vertical = 4.dp, horizontal = 2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val trackHeight = size.height.toFloat()
                            val thumbHeight = (trackHeight * 0.35f).coerceAtLeast(24f)
                            val maxTranslation = trackHeight - thumbHeight
                            val maxScroll = scrollState.maxValue.toFloat()

                            if (maxTranslation > 0f && maxScroll > 0f) {
                                val scrollDelta = dragAmount.y * (maxScroll / maxTranslation)
                                coroutineScope.launch {
                                    scrollState.scrollTo((scrollState.value + scrollDelta.toInt()).coerceIn(0, scrollState.maxValue))
                                }
                            }
                        }
                    }
            ) {
                val trackHeight = this.maxHeight
                val thumbHeight = (trackHeight * 0.35f).coerceAtLeast(24.dp)
                val maxTranslation = trackHeight - thumbHeight

                val progress = if (scrollState.maxValue > 0) {
                    (scrollState.value.toFloat() / scrollState.maxValue.toFloat()).coerceIn(0f, 1f)
                } else 0f

                val thumbTranslationY = maxTranslation * progress

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(thumbHeight)
                        .graphicsLayer {
                            translationY = thumbTranslationY.toPx()
                        }
                        .clip(RoundedCornerShape(4.dp))
                        .background(selectedColor.copy(alpha = 0.7f))
                )
            }
        }
    }
}

@Composable
fun DeadlinesScreen(
    selectedColor: Color,
    tableData: Map<String, String>,
    initialCourseName: String? = null,
    onConsumedInitialCourse: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var deadlinesList by remember { mutableStateOf(loadDeadlines(context)) }

    // Sub-tab: 0 -> تکالیف ("homework"), 1 -> میان‌ترم ("midterm"), 2 -> پایان‌ترم ("final")
    var selectedSubTab by remember { mutableStateOf(0) }
    val categories = listOf(
        "homework" to "تکالیف",
        "midterm" to "میان\u200Cترم",
        "final" to "پایان\u200Cترم"
    )

    val pagerState = rememberPagerState(pageCount = { categories.size })
    val coroutineScope = rememberCoroutineScope()

    var isJumping0To2 by remember { mutableStateOf(false) }
    var isJumping2To0 by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        if (!isJumping0To2 && !isJumping2To0) {
            if (selectedSubTab != pagerState.currentPage) {
                selectedSubTab = pagerState.currentPage
            }
        }
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<DeadlineItem?>(null) }
    var itemToDelete by remember { mutableStateOf<DeadlineItem?>(null) }

    LaunchedEffect(initialCourseName) {
        if (initialCourseName != null) {
            editingItem = null
            showAddDialog = true
        }
    }

    // Course list from tableData + "آزاد"
    val availableCourses = remember(tableData) {
        val extracted = tableData.entries
            .filter { it.key.startsWith("block_") && it.value.isNotBlank() }
            .map { it.value.lines().firstOrNull()?.trim() ?: "" }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
        extracted + "آزاد"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Sub-Tabs (3 Tabs: تکالیف, میان‌ترم, پایان‌ترم) at the very top
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = selectedColor.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, selectedColor.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    categories.forEachIndexed { index, (catKey, label) ->
                        val isSelected = selectedSubTab == index
                        val count = deadlinesList.count { it.category == catKey && !it.isCompleted }
                        val activeColor = selectedColor

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { 
                                    val previousSubTab = selectedSubTab
                                    selectedSubTab = index
                                    coroutineScope.launch {
                                        if (previousSubTab == 0 && index == 2) {
                                            isJumping0To2 = true
                                            pagerState.scrollToPage(1)
                                            pagerState.animateScrollToPage(
                                                page = 2,
                                                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                                            )
                                            isJumping0To2 = false
                                        } else if (previousSubTab == 2 && index == 0) {
                                            isJumping2To0 = true
                                            pagerState.scrollToPage(1)
                                            pagerState.animateScrollToPage(
                                                page = 0,
                                                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                                            )
                                            isJumping2To0 = false
                                        } else {
                                            pagerState.animateScrollToPage(
                                                page = index,
                                                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                                            )
                                        }
                                    }
                                },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) activeColor else Color.Transparent,
                            contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                                if (count > 0) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(
                                                color = if (isSelected) Color.White.copy(alpha = 0.3f) else activeColor.copy(alpha = 0.15f),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$count",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else activeColor,
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(
                                                platformStyle = androidx.compose.ui.text.PlatformTextStyle(
                                                    includeFontPadding = false
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                val targetCategoryIndex = when {
                    isJumping0To2 && page == 1 -> 0
                    isJumping2To0 && page == 1 -> 2
                    else -> page
                }
                val catKey = categories[targetCategoryIndex].first
                val catLabel = categories[targetCategoryIndex].second
                val currentItems = deadlinesList
                    .filter { it.category == catKey }
                    .sortedWith(
                        compareBy<DeadlineItem> { it.isCompleted }
                            .thenBy {
                                val ts = if (it.dueTimestamp != 0L) it.dueTimestamp else parseDueTimestamp(it.dueDate, it.dueTime)
                                if (ts == 0L) Long.MAX_VALUE else ts
                            }
                            .thenByDescending { it.createdAt }
                    )

                if (currentItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            onClick = {
                                editingItem = null
                                showAddDialog = true
                            },
                            shape = CircleShape,
                            color = selectedColor.copy(alpha = 0.15f),
                            border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.5f)),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "افزودن",
                                    tint = selectedColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                } else {
                    ScrollableListWithThumb(
                        selectedColor = selectedColor,
                        itemCount = currentItems.size,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        currentItems.forEach { item ->
                            DeadlineCard(
                                item = item,
                                selectedColor = selectedColor,
                                onToggleComplete = {
                                    val updated = deadlinesList.map {
                                        if (it.id == item.id) it.copy(isCompleted = !it.isCompleted) else it
                                    }
                                    deadlinesList = updated
                                    saveDeadlines(context, updated)
                                },
                                onEdit = {
                                    editingItem = item
                                    showAddDialog = true
                                },
                                onDelete = {
                                    itemToDelete = item
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                onClick = {
                                    editingItem = null
                                    showAddDialog = true
                                },
                                shape = CircleShape,
                                color = selectedColor.copy(alpha = 0.15f),
                                border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.5f)),
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "افزودن $catLabel جدید",
                                        tint = selectedColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    // Add / Edit Dialog
    if (showAddDialog) {
        AddEditDeadlineDialog(
            initialItem = editingItem,
            initialCategory = categories[selectedSubTab].first,
            initialCourseName = initialCourseName,
            availableCourses = availableCourses,
            selectedColor = selectedColor,
            onDismiss = {
                showAddDialog = false
                editingItem = null
                onConsumedInitialCourse()
            },
            onSave = { newItem ->
                val updated = if (editingItem != null) {
                    deadlinesList.map { if (it.id == newItem.id) newItem else it }
                } else {
                    deadlinesList + newItem
                }
                deadlinesList = updated
                saveDeadlines(context, updated)
                showAddDialog = false
                editingItem = null
                onConsumedInitialCourse()
            },
            onDelete = if (editingItem != null) {
                {
                    val itemToDel = editingItem
                    showAddDialog = false
                    editingItem = null
                    onConsumedInitialCourse()
                    if (itemToDel != null) {
                        val updated = deadlinesList.filter { it.id != itemToDel.id }
                        deadlinesList = updated
                        saveDeadlines(context, updated)
                    }
                }
            } else null
        )
    }

    // Delete confirmation dialog
    if (itemToDelete != null) {
        RtlAlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("حذف ددلاین", fontWeight = FontWeight.Bold) },
            text = { Text("آیا از حذف «${itemToDelete?.title}» اطمینان دارید؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val updated = deadlinesList.filter { it.id != itemToDelete?.id }
                        deadlinesList = updated
                        saveDeadlines(context, updated)
                        itemToDelete = null
                    }
                ) {
                    Text("حذف", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("لغو")
                }
            }
        )
    }
}

@Composable
fun DeadlineCard(
    item: DeadlineItem,
    selectedColor: Color,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val cardColor = if (item.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else selectedColor
    val contentColor = if (item.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else Color.White

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        shape = RoundedCornerShape(6.dp),
        color = cardColor,
        border = BorderStroke(
            width = 1.dp,
            color = if (item.isCompleted) MaterialTheme.colorScheme.outline.copy(alpha = 0.2f) else selectedColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox button
            IconButton(
                onClick = onToggleComplete,
                modifier = Modifier.size(32.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (item.isCompleted) selectedColor else Color.Transparent,
                    border = BorderStroke(2.dp, if (item.isCompleted) selectedColor else Color.White.copy(alpha = 0.8f)),
                    modifier = Modifier.size(20.dp)
                ) {
                    if (item.isCompleted) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "تکمیل شده",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Main Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Title and Course Tag Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                        style = if (item.isCompleted) TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough) else TextStyle.Default,
                        modifier = Modifier.weight(1f)
                    )

                    if (item.courseName.isNotBlank() && item.courseName != "آزاد") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.25f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = item.courseName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = contentColor,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                val dateText = when {
                    item.dueDate.isNotBlank() && item.dueTime.isNotBlank() -> {
                        if (item.dueDate.contains("ساعت")) item.dueDate else "${item.dueDate} - ساعت ${item.dueTime}"
                    }
                    item.dueDate.isNotBlank() -> item.dueDate
                    item.dueTime.isNotBlank() -> "ساعت ${item.dueTime}"
                    else -> "بدون تاریخ موعد"
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = contentColor.copy(alpha = 0.9f),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = dateText,
                            fontSize = 11.sp,
                            color = contentColor.copy(alpha = 0.9f)
                        )
                        if (item.hasNotification) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "دارای اعلان",
                                tint = contentColor.copy(alpha = 0.9f),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    if (item.description.isNotBlank()) {
                        Text(
                            text = item.description,
                            fontSize = 11.sp,
                            color = contentColor.copy(alpha = 0.8f),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditDeadlineDialog(
    initialItem: DeadlineItem?,
    initialCategory: String,
    initialCourseName: String? = null,
    availableCourses: List<String>,
    selectedColor: Color,
    onDismiss: () -> Unit,
    onSave: (DeadlineItem) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(initialItem?.title ?: "") }
    val selectedCategory = initialItem?.category ?: initialCategory
    var selectedCourseName by remember { 
        mutableStateOf(initialItem?.courseName ?: initialCourseName ?: "آزاد") 
    }
    var dueDate by remember {
        mutableStateOf(
            if (!initialItem?.dueDate.isNullOrBlank()) initialItem!!.dueDate
            else JalaliCalendarHelper.getJalaliDateOneWeekLaterString()
        )
    }
    var dueTime by remember { mutableStateOf(initialItem?.dueTime ?: "23:00") }
    var hasNotification by remember { mutableStateOf(initialItem?.hasNotification ?: false) }
    var description by remember { mutableStateOf(initialItem?.description ?: "") }
    var showError by remember { mutableStateOf(false) }
    var showTagSelectionDialog by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    val isEditing = initialItem != null
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(150)
        focusRequester.requestFocus()
    }

    RtlAlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditing) "ویرایش ددلاین" else "افزودن ددلاین جدید",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = selectedColor
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) showError = false
                    },
                    label = { Text("عنوان") },
                    isError = showError,
                    singleLine = false,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    shape = RoundedCornerShape(10.dp)
                )
                if (showError) {
                    Text("لطفا عنوان را وارد کنید", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                }

                // Tag Selector Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable { showTagSelectionDialog = true }
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = selectedColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Text("انتخاب تگ", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = selectedColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, selectedColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = if (selectedCourseName.isNotBlank()) selectedCourseName else "آزاد",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = selectedColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                if (showTagSelectionDialog) {
                    RtlAlertDialog(
                        onDismissRequest = { showTagSelectionDialog = false },
                        title = {
                            Text(
                                text = "انتخاب تگ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = selectedColor
                            )
                        },
                        text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                availableCourses.forEach { course ->
                                    val isSelected = selectedCourseName == course
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedCourseName = course
                                                showTagSelectionDialog = false
                                            },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) selectedColor.copy(alpha = 0.15f) else Color.Transparent,
                                        border = BorderStroke(
                                            1.dp,
                                            if (isSelected) selectedColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = course,
                                                fontSize = 13.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurface
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = selectedColor,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {},
                        dismissButton = {
                            TextButton(onClick = { showTagSelectionDialog = false }) {
                                Text("انصراف")
                            }
                        }
                    )
                }

                // Date Picker Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable { showDatePickerDialog = true }
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = selectedColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Text("تاریخ ددلاین", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = selectedColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, selectedColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = if (dueDate.isNotBlank()) dueDate else "انتخاب تاریخ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = selectedColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                if (showDatePickerDialog) {
                    val (parsedY, parsedM, parsedD) = JalaliCalendarHelper.parseJalaliDateString(dueDate)
                    CustomDatePickerDialog(
                        initialYear = parsedY,
                        initialMonth = parsedM,
                        initialDay = parsedD,
                        selectedColor = selectedColor,
                        onDismiss = { showDatePickerDialog = false },
                        onConfirm = { year, month, day ->
                            dueDate = JalaliCalendarHelper.formatJalaliDate(year, month, day)
                            showDatePickerDialog = false
                        }
                    )
                }

                // Time Picker Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .clickable { showTimePickerDialog = true }
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = selectedColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Text("ساعت ددلاین", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = selectedColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, selectedColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = dueTime,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = selectedColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                if (showTimePickerDialog) {
                    val timeParts = dueTime.split(':')
                    val h = timeParts.getOrNull(0)?.toIntOrNull() ?: 12
                    val m = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
                    CustomTimePickerDialog(
                        initialHour = h,
                        initialMinute = m,
                        selectedColor = selectedColor,
                        onDismiss = { showTimePickerDialog = false },
                        onConfirm = { hour, minute ->
                            dueTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                            showTimePickerDialog = false
                        }
                    )
                }

                // Notification Toggle Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { hasNotification = !hasNotification }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("فعال‌سازی اعلان یادآوری", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = hasNotification,
                        onCheckedChange = { hasNotification = it },
                        colors = if (isSystemInDarkTheme()) SwitchDefaults.colors(checkedThumbColor = Color.White, uncheckedThumbColor = Color.White) else SwitchDefaults.colors()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        showError = true
                    } else {
                        val timestamp = parseDueTimestamp(dueDate, dueTime)
                        val newItem = DeadlineItem(
                            id = initialItem?.id ?: java.util.UUID.randomUUID().toString(),
                            title = title.trim(),
                            courseName = selectedCourseName,
                            category = selectedCategory,
                            dueDate = dueDate.trim(),
                            dueTime = dueTime.trim(),
                            dueTimestamp = timestamp,
                            hasNotification = hasNotification,
                            description = description.trim(),
                            isCompleted = initialItem?.isCompleted ?: false,
                            createdAt = initialItem?.createdAt ?: System.currentTimeMillis()
                        )
                        scheduleDeadlineAlarm(context, newItem)
                        onSave(newItem)
                    }
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("ذخیره", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            if (onDelete != null) {
                TextButton(onClick = onDelete) {
                    Text("حذف", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

fun copyToClipboard(context: Context, label: String, text: String) {
    if (text.isBlank()) return
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "$label کپی شد: $text", Toast.LENGTH_SHORT).show()
}

fun openLocationInMap(context: Context, latitude: Double, longitude: Double, label: String) {
    val geoUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(${Uri.encode(label)})")
    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
    try {
        context.startActivity(mapIntent)
    } catch (e: Exception) {
        val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        try {
            context.startActivity(webIntent)
        } catch (ex: Exception) {
            Toast.makeText(context, "برنامه‌ای برای باز کردن مکان یافت نشد", Toast.LENGTH_SHORT).show()
        }
    }
}

fun openTelegramLink(context: Context, url: String) {
    if (url.isBlank()) return
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "امکان باز کردن لینک وجود ندارد", Toast.LENGTH_SHORT).show()
    }
}

fun openContactLink(context: Context, label: String, value: String) {
    val openUrl = when (label) {
        "ایتا" -> "https://eitaa.com/${value.removePrefix("@").trim()}"
        "تلگرام" -> "https://t.me/${value.removePrefix("@").trim()}"
        "شماره تماس" -> "tel:${value.trim()}"
        else -> "mailto:${value.trim()}"
    }
    if (label == "تلگرام") {
        openTelegramLink(context, openUrl)
    } else {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(openUrl))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "امکان باز کردن وجود ندارد", Toast.LENGTH_SHORT).show()
        }
    }
}

fun openPdfFromAssets(context: Context, assetFileName: String) {
    try {
        val file = java.io.File(context.cacheDir, "phone_numbers.pdf")
        context.assets.open(assetFileName).use { input ->
            java.io.FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: android.content.ActivityNotFoundException) {
        Toast.makeText(context, "برنامه‌ای برای مشاهده فایل PDF یافت نشد", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "خطا در باز کردن فایل PDF", Toast.LENGTH_SHORT).show()
    }
}


@Composable
fun ThreeDButton(
    onClick: () -> Unit,
    baseColor: Color,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 14.dp,
    content: @Composable androidx.compose.foundation.layout.BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(baseColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun CategoryScrollBox(
    itemCount: Int,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    maxRows: Int = 3,
    rowHeight: Dp = 65.dp,
    rowSpacing: Dp = 10.dp,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    val needsScroll = itemCount > (maxRows * 2)

    if (!needsScroll) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(rowSpacing)
        ) {
            content()
        }
    } else {
        val maxHeightDp = (rowHeight * maxRows) + (rowSpacing * (maxRows - 1))
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()

        Row(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(max = maxHeightDp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(rowSpacing)
            ) {
                content()
            }

            BoxWithConstraints(
                modifier = Modifier
                    .width(14.dp)
                    .fillMaxHeight()
                    .padding(vertical = 2.dp, horizontal = 3.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val trackHeight = size.height.toFloat()
                            val thumbHeight = (trackHeight * 0.35f).coerceAtLeast(24f)
                            val maxTranslation = trackHeight - thumbHeight
                            val maxScroll = scrollState.maxValue.toFloat()

                            if (maxTranslation > 0f && maxScroll > 0f) {
                                val scrollDelta = dragAmount.y * (maxScroll / maxTranslation)
                                coroutineScope.launch {
                                    scrollState.scrollTo((scrollState.value + scrollDelta.toInt()).coerceIn(0, scrollState.maxValue))
                                }
                            }
                        }
                    }
            ) {
                val trackHeight = this.maxHeight
                val thumbHeight = (trackHeight * 0.35f).coerceAtLeast(24.dp)
                val maxTranslation = trackHeight - thumbHeight

                val progress = if (scrollState.maxValue > 0) {
                    (scrollState.value.toFloat() / scrollState.maxValue.toFloat()).coerceIn(0f, 1f)
                } else 0f

                val thumbTranslationY = maxTranslation * progress

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(thumbHeight)
                        .graphicsLayer {
                            translationY = thumbTranslationY.toPx()
                        }
                        .clip(RoundedCornerShape(4.dp))
                        .background(selectedColor)
                )
            }
        }
    }
}

fun sortContacts(list: List<ContactInfo>): List<ContactInfo> {
    val nonProfessors = list.filter { 
        !it.title.contains("مدیر گروه") && 
        it.title != "استاد" 
    }
    val departmentHeads = list.filter { 
        it.title.contains("مدیر گروه") 
    }
    val professors = list.filter { 
        it.title == "استاد" 
    }
    
    val result = mutableListOf<ContactInfo>()
    result.addAll(nonProfessors)
    result.addAll(departmentHeads)
    result.addAll(professors)
    return result
}

fun loadContacts(context: Context): List<ContactInfo> {
    val prefs = context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE)
    val jsonString = prefs.getString("saved_contacts_v1", null)
    val defaultList = sortContacts(listOf(
        // === ۱. غیر اساتید ===
        ContactInfo(
            title = "امور دانشجویی",
            name = "آقای رمضانی",
            phone = "09382747886",
            telegram = "@public_shrun",
            eitaa = "@iman2015ra"
        ),
        ContactInfo(
            id = "c1_food",
            title = "امور تغذیه",
            name = "آقای طبائیان",
            phone = "09130261474",
            eitaa = "@mojtaba_tabaeian"
        ),
        ContactInfo(
            id = "c2_lotfi",
            title = "امور آزمایشگاه",
            name = "لطفی",
            phone = "09300982275"
        ),
        ContactInfo(
            title = "امور خوابگاه",
            name = "کسائی",
            telegram = "@Kasaie7"
        ),
        ContactInfo(
            title = "امور آزمایشگاه",
            name = "عسگری",
            phone = "09015270112"
        ),
        ContactInfo(
            title = "حراست",
            name = "مدنی",
            phone = "09966129958"
        ),
        ContactInfo(
            title = "امور مالی اداری",
            name = "خانم عشقی",
            eitaa = "@M_Eshghi1"
        ),
        ContactInfo(
            title = "خدمات آموزشی",
            name = "علیدادی",
            telegram = "@Ahmad_27"
        ),
        ContactInfo(
            title = "امور آموزش",
            name = "رفعت",
            eitaa = "@Sadegh140000"
        ),
        ContactInfo(
            title = "امور پژوهش",
            name = "مهرداد قریشی",
            telegram = "@SMGH77",
            eitaa = "@SMGH77"
        ),
        ContactInfo(
            title = "فناوری اطلاعات",
            name = "مجید منصف",
            phone = "09133229800"
        ),
        ContactInfo(
            title = "دفتر ریاست",
            name = "خانم صلاحی",
            eitaa = "@salahi110"
        ),

        // === ۲. مدیر گروه‌ها ===
        ContactInfo(
            title = "مدیر گروه کامپیوتر",
            name = "دکتر شمس",
            telegram = "@mrz_shams",
            eitaa = "@mrz_shams"
        ),
        ContactInfo(
            title = "مدیر گروه مکانیک",
            name = "دکتر مهرور",
            eitaa = "@alimehrvar",
            email = "mech.shr@gmail.com"
        ),
        ContactInfo(
            title = "مدیر گروه برق",
            name = "دکتر انصاری",
            telegram = "@mr_ansari_sh",
            eitaa = "@mr_ansari_sh"
        ),
        ContactInfo(
            title = "مدیر گروه علوم مهندسی",
            name = "دکتر مرادزاده",
            eitaa = "@moradzadehdehkordi",
            email = "a.moradzadeh@shr.ui.ac.ir"
        ),
        ContactInfo(
            title = "مدیر گروه دروس عمومی",
            name = "حجة‌الاسلام تاکی",
            eitaa = "@mmm6164"
        ),

        // === ۳. اساتید ===
        ContactInfo(
            title = "استاد",
            name = "بابک پرتوی",
            eitaa = "@Babak_Partovi"
        ),
        ContactInfo(
            title = "استاد",
            name = "افشاری",
            telegram = "@Shr_Students"
        ),
        ContactInfo(
            title = "استاد",
            name = "مصطفی کاظمی",
            telegram = "@mostafa_kazemi"
        ),
        ContactInfo(
            title = "استاد",
            name = "شکوفه مقیمی",
            telegram = "@eyesun72"
        ),
        ContactInfo(
            title = "استاد",
            name = "صادق مطهر",
            telegram = "@smotahar"
        ),
        ContactInfo(
            title = "استاد",
            name = "روزمند",
            telegram = "@OR23451",
            email = "roozmand@gmail.com"
        ),
        ContactInfo(
            title = "استاد",
            name = "جان‌نثاری",
            telegram = "@Mjanlad",
            eitaa = "@hojald",
            email = "mjanesari79@gmail.com"
        ),
        ContactInfo(
            title = "استاد",
            name = "امین حمیدی",
            phone = "09132754105"
        ),
        ContactInfo(
            title = "استاد",
            name = "معتمدی",
            eitaa = "@motamedi1400",
            email = "m.motamedi110@gmail.com"
        ),
        ContactInfo(
            title = "استاد",
            name = "آرش نجمائی",
            telegram = "@Nj96A"
        ),
        ContactInfo(
            title = "استاد",
            name = "صدرایی",
            email = "sadraei.pnu@gmail.com"
        ),
        ContactInfo(
            title = "استاد",
            name = "علی صلاتی",
            telegram = "@AliSalaty"
        ),
        ContactInfo(
            title = "استاد",
            name = "محمدحسن امیریون",
            telegram = "@MH_Amirioun",
            eitaa = "@MH_Amirioun"
        ),
        ContactInfo(
            title = "استاد",
            name = "کریم رحیمیان",
            telegram = "@karim_rahimian"
        ),
        ContactInfo(
            title = "استاد",
            name = "میرطاووسی",
            eitaa = "@SAmtmir"
        ),
        ContactInfo(
            title = "استاد",
            name = "زهرا وهابی",
            eitaa = "@zvahu7"
        ),
        ContactInfo(
            title = "استاد",
            name = "حامد باقری",
            telegram = "@hbagheri568",
            email = "hamedbagheriesfe@gmail.com"
        ),
        ContactInfo(
            title = "استاد",
            name = "احمد رئیسی",
            telegram = "@Raeisidehkordi76"
        ),
        ContactInfo(
            title = "استاد",
            name = "حمیدرضا اختر",
            phone = "09132222092"
        ),
        ContactInfo(
            title = "استاد",
            name = "امیر بهرامی",
            telegram = "@Amir_bhr00",
            eitaa = "@Dr_Kh_B_Q_36"
        ),
        ContactInfo(
            title = "استاد",
            name = "ناجی",
            telegram = "@M_N_E"
        ),
        ContactInfo(
            title = "استاد",
            name = "سینا تاکی",
            telegram = "@sinataki"
        ),
        ContactInfo(
            title = "استاد",
            name = "محسن جنتی",
            telegram = "@DrMJannati"
        ),
        ContactInfo(
            title = "استاد",
            name = "محسن تاکی",
            telegram = "@T_M_T_M"
        ),
        ContactInfo(
            title = "استاد",
            name = "علی خانی",
            eitaa = "@monji031"
        ),
        ContactInfo(
            title = "استاد",
            name = "همائی",
            telegram = "@homaei_b"
        ),
        ContactInfo(
            title = "استاد",
            name = "طبیبیان",
            telegram = "@behnaz_tabibian"
        ),
        ContactInfo(
            title = "استاد",
            name = "میرمسیب",
            telegram = "@E_m1369"
        ),
        ContactInfo(
            title = "استاد",
            name = "دکتر نصیبی",
            telegram = "@dr_nasibi"
        ),
        ContactInfo(
            title = "استاد",
            name = "مهران تاکی",
            telegram = "@Mt_phys"
        ),
        ContactInfo(
            title = "استاد",
            name = "مدنی",
            eitaa = "@a_b_2_3_d_4"
        ),
        ContactInfo(
            title = "استاد",
            name = "رحمتی",
            telegram = "@Rahmati_physics"
        ),
        ContactInfo(
            title = "استاد",
            name = "طباطبایی",
            telegram = "@SSepehrT"
        ),
        ContactInfo(
            title = "استاد",
            name = "علی مختاری",
            eitaa = "@Ali_Mokhtari_K"
        ),
        ContactInfo(
            title = "استاد",
            name = "محمد قربانی",
            eitaa = "@moha_ghorbani"
        ),
        ContactInfo(
            title = "استاد",
            name = "محمدرضا ستاینده",
            eitaa = "@Mrsetayandeh"
        ),
        ContactInfo(
            title = "استاد",
            name = "مریم لطفی",
            eitaa = "@LotfiEitaa"
        ),
        ContactInfo(
            title = "استاد",
            name = "مریم حسینی پژوه",
            eitaa = "@ho_m_et"
        ),
        ContactInfo(
            title = "استاد",
            name = "سعید افشاری",
            eitaa = "@DrAfshari"
        ),
        ContactInfo(
            title = "استاد",
            name = "ایمان زارع",
            eitaa = "@Zare_Iman"
        )
    ))

    if (jsonString.isNullOrBlank()) {
        return defaultList
    }
    return try {
        val array = org.json.JSONArray(jsonString)
        val list = mutableListOf<ContactInfo>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                ContactInfo(
                    id = obj.optString("id", java.util.UUID.randomUUID().toString()),
                    title = obj.optString("title", ""),
                    name = obj.optString("name", ""),
                    phone = obj.optString("phone", ""),
                    telegram = obj.optString("telegram", ""),
                    eitaa = obj.optString("eitaa", ""),
                    email = obj.optString("email", "")
                )
            )
        }

        var isModified = false
        for (defaultContact in defaultList) {
            val idx = list.indexOfFirst { existing ->
                (defaultContact.id.isNotBlank() && existing.id == defaultContact.id) ||
                (defaultContact.eitaa.isNotBlank() && existing.eitaa == defaultContact.eitaa) ||
                (defaultContact.telegram.isNotBlank() && existing.telegram == defaultContact.telegram) ||
                (defaultContact.phone.isNotBlank() && existing.phone == defaultContact.phone) ||
                (defaultContact.email.isNotBlank() && existing.email == defaultContact.email) ||
                (existing.name == defaultContact.name && existing.title == defaultContact.title)
            }
            if (idx != -1) {
                val existing = list[idx]
                if (existing.title != defaultContact.title || existing.name != defaultContact.name) {
                    list[idx] = existing.copy(
                        title = defaultContact.title,
                        name = defaultContact.name
                    )
                    isModified = true
                }
            } else {
                list.add(defaultContact)
                isModified = true
            }
        }

        val sortedList = sortContacts(list)
        if (isModified) {
            saveContacts(context, sortedList)
        }

        if (sortedList.isEmpty()) defaultList else sortedList
    } catch (e: Exception) {
        defaultList
    }
}

fun saveContacts(context: Context, contacts: List<ContactInfo>) {
    val prefs = context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE)
    val array = org.json.JSONArray()
    for (contact in contacts) {
        val obj = org.json.JSONObject()
        obj.put("id", contact.id)
        obj.put("title", contact.title)
        obj.put("name", contact.name)
        obj.put("phone", contact.phone)
        obj.put("telegram", contact.telegram)
        obj.put("eitaa", contact.eitaa)
        obj.put("email", contact.email)
        array.put(obj)
    }
    prefs.edit().putString("saved_contacts_v1", array.toString()).apply()
}

@Composable
fun MainScreen(
    selectedColor: Color,
    onColorChange: (Color) -> Unit,
    darkModeMode: String,
    onDarkModeModeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            scheduleAllAlarms(context)
        }
    }

    LaunchedEffect(Unit) {
        val hasRequestedPermission = prefs.getBoolean("has_requested_first_notification_permission", false)
        if (!hasRequestedPermission) {
            prefs.edit().putBoolean("has_requested_first_notification_permission", true).apply()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    scheduleAllAlarms(context)
                }
            } else {
                scheduleAllAlarms(context)
            }
        }
    }

    val rainbowColors = remember {
        listOf(
            Color(0xFFF44336), // Red
            Color(0xFFFF9800), // Orange
            Color(0xFFFFB300), // Amber Gold
            Color(0xFF4CAF50), // Green
            Color(0xFF009688), // Teal
            Color(0xFF00BCD4), // Cyan / Sky Blue
            Color(0xFF2196F3), // Blue
            Color(0xFF3F51B5), // Indigo
            Color(0xFF7E57C2), // Purple / Violet
            Color(0xFFEC407A)  // Soft Pink
        )
    }

    val defaultSites = remember {
        listOf(
            WebSite("گلستان", "https://golestan.ui.ac.ir/forms/authenticateuser/main.htm"),
            WebSite("LMS", "https://lms5.ui.ac.ir/"),
            WebSite("کالینان", "https://shrfood.ui.ac.ir/Reservation/Reservation.aspx"),
            WebSite("سایت دانشگاه", "https://shr.ui.ac.ir")
        )
    }
    var sitesList by remember { mutableStateOf(defaultSites) }

    val defaultTelegramChannels = remember {
        listOf(
            TelegramChannel("کافه سوال", "@shahreza_uni", "https://t.me/shahreza_uni"),
            TelegramChannel("کانال دانشجویی", "@ui_shr", "https://t.me/ui_shr", eitaaUrl = "https://eitaa.com/ui_shr"),
            TelegramChannel("شورای صنفی", "@senfi_shr", "https://t.me/senfi_shr"),
            TelegramChannel("هم‌مسیر", "@shahrezaunicar", "https://t.me/shahrezaunicar")
        )
    }
    var telegramChannelsList by remember { mutableStateOf(defaultTelegramChannels) }

    var editingSite by remember { mutableStateOf<WebSite?>(null) }
    var showSiteDialog by remember { mutableStateOf(false) }

    var editingChannel by remember { mutableStateOf<TelegramChannel?>(null) }
    var showChannelDialog by remember { mutableStateOf(false) }
    var activeChannelChooser by remember { mutableStateOf<TelegramChannel?>(null) }

    var showAnnouncementDialog by remember { mutableStateOf(false) }
    var announcementDialogTitle by remember { mutableStateOf("") }
    var announcementDialogMessage by remember { mutableStateOf("") }

    var contactsList by remember { mutableStateOf(loadContacts(context)) }
    var activeDetailDialog by remember { mutableStateOf<ContactDetailDialogState?>(null) }
    var activeContactChooser by remember { mutableStateOf<ContactInfo?>(null) }
    var showAddContactDialog by remember { mutableStateOf(false) }
    var editingContact by remember { mutableStateOf<ContactInfo?>(null) }
    
    var isStaffSearchVisible by remember { mutableStateOf(false) }
    var staffSearchQuery by remember { mutableStateOf("") }
    var isGolestanSearchVisible by remember { mutableStateOf(false) }
    var golestanSearchQuery by remember { mutableStateOf("") }

    var showColorPicker by remember { mutableStateOf(false) }
    
    var isWeekParityFlipped by remember {
        mutableStateOf(prefs.getBoolean("isWeekParityFlipped", false))
    }

    var isSetupCompleted by remember {
        val hasCompleted = prefs.contains("is_setup_completed") || prefs.contains("config_visible_days") || prefs.all.keys.any { it.startsWith("block_") }
        mutableStateOf(hasCompleted)
    }

    var showFirstTimeDialog by remember { mutableStateOf(false) }
    var showGolestanMessageDialog by remember { mutableStateOf(false) }
    var showAddScheduleChoiceDialog by remember { mutableStateOf(false) }
    var showGolestanSyncDialog by remember { mutableStateOf(false) }
    var showPdfGuideDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    var isExtractingPdf by remember { mutableStateOf(false) }
    var pdfExtractionResult by remember { mutableStateOf<PdfExtractionResult?>(null) }
    var showPdfResultDialog by remember { mutableStateOf(false) }

    var parsedCourses by remember { mutableStateOf<List<ParsedCourse>?>(null) }
    var showParsedCoursesDialog by remember { mutableStateOf(false) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf(0) } // 0: Timetable, 1: Deadlines, 2: Websites, 3: Settings
    var expandedSitesSection by remember { mutableStateOf<Int?>(4) }
    var prefilledDeadlineCourse by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(expandedSitesSection) {
        if (expandedSitesSection != 0) {
            activeContactChooser = null
            isStaffSearchVisible = false
            staffSearchQuery = ""
        }
        if (expandedSitesSection != 2) {
            activeChannelChooser = null
        }
        if (expandedSitesSection != 3) {
            isGolestanSearchVisible = false
            golestanSearchQuery = ""
        }
    }

    // Supabase States
    var remoteAppConfig by remember { mutableStateOf<AppConfigRemote?>(null) }
    var showAppUpdateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val todayDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
        val lastSyncDate = prefs.getString("last_server_sync_date", "")

        // بروزرسانی اطلاعات از سرور فقط روزی یک بار با باز شدن اپلیکیشن انجام می‌شود
        if (lastSyncDate != todayDate) {
            // ۱. ثبت یا آپدیت آمار مشخصات و ورود کاربر/دستگاه در سوپابیس
            SupabaseRepository.registerOrUpdateUser(context)

            // ۲. دریافت کانفیگ جهت بررسی آپدیت نسخه و اعمال وضعیت هفته زوج/فرد
            val configResult = SupabaseRepository.fetchAppConfig()
            val config = configResult.getOrNull()
            if (config != null) {
                remoteAppConfig = config
                if (config.isWeekFlipped != isWeekParityFlipped) {
                    isWeekParityFlipped = config.isWeekFlipped
                    prefs.edit().putBoolean("isWeekParityFlipped", config.isWeekFlipped).apply()
                }

                if (config.latestVersionCode > BuildConfig.VERSION_CODE) {
                    showAppUpdateDialog = true
                }
            }

            // ذخیره تاریخ امروز جهت جلوگیری از بروزرسانی مجدد در همان روز
            prefs.edit().putString("last_server_sync_date", todayDate).apply()
        }
    }

    // Table Setup states
    val fullDaysList = remember { listOf("شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه") }
    val manualDaysList = remember { listOf("شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه") }
    
    val fullHoursGolestan = remember {
        listOf(
            "8:00", "9:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00", "17:00"
        )
    }

    val fullHoursHolding = remember {
        listOf(
            "8:30", "10:15", "12:30", "14:15", "16:00"
        )
    }

    var scheduleMode by remember {
        mutableStateOf(prefs.getString("schedule_mode", "golestan") ?: "golestan")
    }

    // Default configuration: Sunday, Monday, Tuesday checked by default
    val activeDays = remember {
        val saved = prefs.getString("config_visible_days", "یکشنبه,دوشنبه,سه‌شنبه") ?: "یکشنبه,دوشنبه,سه‌شنبه"
        saved.split(",").filter { it.isNotEmpty() }.toMutableStateList()
    }

    val activeHoursGolestan = remember {
        val defaultHours = "8:00,9:00,10:00,11:00,12:00,13:00,14:00,15:00,16:00,17:00"
        val saved = prefs.getString("config_visible_hours_golestan", null)
            ?: prefs.getString("config_visible_hours", defaultHours)
            ?: defaultHours
        saved.split(",").filter { it.isNotEmpty() && fullHoursGolestan.contains(it) }.toMutableStateList().apply {
            if (isEmpty()) addAll(fullHoursGolestan)
        }
    }

    val activeHoursHolding = remember {
        val defaultHours = "8:30,10:15,12:30,14:15,16:00"
        val saved = prefs.getString("config_visible_hours_holding", defaultHours) ?: defaultHours
        saved.split(",").filter { it.isNotEmpty() && fullHoursHolding.contains(it) }.toMutableStateList().apply {
            if (isEmpty()) addAll(fullHoursHolding)
        }
    }

    val isHoldingMode = scheduleMode.startsWith("holding")
    val fullHoursMap = if (isHoldingMode) fullHoursHolding else fullHoursGolestan
    val activeHours = if (isHoldingMode) activeHoursHolding else activeHoursGolestan
    val tableModeForTable = if (isHoldingMode) "holding" else "golestan"

    val tableData = remember {
        mutableStateMapOf<String, String>().apply {
            prefs.all.forEach { (key, value) ->
                if ((key.startsWith("block_") || key.startsWith("duration_") || key.startsWith("room_")) && value is String) {
                    put(key, value)
                }
            }
        }
    }

    val effectiveTableData = remember(tableData.toMap(), scheduleMode, activeDays) {
        when (scheduleMode) {
            "holding_even" -> getHoldingTableData(tableData, isEvenWeek = true, activeDays)
            "holding_odd" -> getHoldingTableData(tableData, isEvenWeek = false, activeDays)
            "holding_all" -> getHoldingTableData(tableData, isEvenWeek = null, activeDays)
            else -> tableData
        }
    }

    val importDetectedCourses: (List<ParsedCourse>) -> Unit = { detected ->
        if (detected.isNotEmpty()) {
            val editor = prefs.edit()
            tableData.keys.toList().forEach { key ->
                editor.remove(key)
            }
            editor.apply()
            tableData.clear()

            val pdfDays = detected.map { it.day }
                .filter { fullDaysList.contains(it) }
                .distinct()
                .sortedWith(compareBy { fullDaysList.indexOf(it) })

            if (pdfDays.isNotEmpty()) {
                activeDays.clear()
                activeDays.addAll(pdfDays)
                prefs.edit().putString("config_visible_days", activeDays.joinToString(",")).apply()
            }

            detected.forEach { course ->
                val slotKey = course.slotKey
                val blockKey = "block_${course.day}_$slotKey"
                val roomKey = "room_${course.day}_$slotKey"
                val durationKey = "duration_${course.day}_$slotKey"

                tableData[blockKey] = course.title
                prefs.edit().putString(blockKey, course.title).apply()

                if (course.room.isNotBlank()) {
                    tableData[roomKey] = course.room
                    prefs.edit().putString(roomKey, course.room).apply()
                }

                if (course.duration > 1) {
                    tableData[durationKey] = course.duration.toString()
                    prefs.edit().putString(durationKey, course.duration.toString()).apply()
                }
            }

            val existingDeadlines = loadDeadlines(context).filter { it.category != "final" }.toMutableList()
            val processedExamTitles = mutableSetOf<String>()
            var examCount = 0

            detected.forEach { course ->
                if (course.examDate.isNotBlank() && !processedExamTitles.contains(course.title)) {
                    processedExamTitles.add(course.title)

                    val eTime = if (course.examTime.isNotBlank()) course.examTime else "12:00"
                    val examDeadline = DeadlineItem(
                        title = "امتحان ${course.title}",
                        courseName = course.title,
                        category = "final",
                        dueDate = course.examDate,
                        dueTime = eTime,
                        dueTimestamp = parseDueTimestamp(course.examDate, eTime),
                        description = ""
                    )

                    existingDeadlines.add(examDeadline)
                    examCount++
                }
            }

            saveDeadlines(context, existingDeadlines)

            prefs.edit().putBoolean("is_setup_completed", true).apply()
            isSetupCompleted = true

            val finalIsEven = isEvenWeekContinuous(isWeekFlipped = isWeekParityFlipped)
            val autoHoldingMode = if (finalIsEven) "holding_even" else "holding_odd"
            scheduleMode = autoHoldingMode
            prefs.edit().putString("schedule_mode", autoHoldingMode).apply()

            val toastMsg = if (examCount > 0) {
                "برنامه درسی و $examCount امتحان پایان‌ترم با موفقیت اضافه شدند"
            } else {
                "برنامه قبلی پاک شد و ${detected.size} درس با موفقیت اضافه شد"
            }
            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
            scheduleFoodReservationAlarm(context, tableData)
            scheduleClassNotifications(context, tableData)
        }
    }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isExtractingPdf = true
            scope.launch {
                try {
                    val result = PdfExtractor.extractText(context, uri)
                    if (result.errorMessage != null) {
                        Toast.makeText(context, result.errorMessage, Toast.LENGTH_LONG).show()
                    } else {
                        val detected = CourseScheduleParser.parse(result.formattedText, result.textItems)
                        if (detected.isNotEmpty()) {
                            importDetectedCourses(detected)
                        } else {
                            Toast.makeText(context, "هیچ درسی در فایل PDF پیدا نشد.", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "خطا در پردازش فایل: ${e.localizedMessage ?: "مشکل ناشناخته"}", Toast.LENGTH_LONG).show()
                } finally {
                    isExtractingPdf = false
                }
            }
        }
    }

    var showTableSettingsPromptDialog by remember { mutableStateOf(false) }
    var showTableSettingsDialog by remember { mutableStateOf(false) }
    var showActiveHoursDialog by remember { mutableStateOf(false) }

    var isClassNotificationEnabled by remember {
        mutableStateOf(prefs.getBoolean("pref_class_notification_enabled", true))
    }

    var isFoodNotificationEnabled by remember {
        mutableStateOf(prefs.getBoolean("pref_food_notification_enabled", true))
    }

    var foodNotificationDay by remember {
        mutableStateOf(prefs.getString("pref_food_notification_day", "auto") ?: "auto")
    }

    var foodNotificationHour by remember {
        mutableStateOf(prefs.getInt("pref_food_notification_hour", 20))
    }

    var foodNotificationMinute by remember {
        mutableStateOf(prefs.getInt("pref_food_notification_minute", 0))
    }

    LaunchedEffect(Unit) {
        scheduleFoodReservationAlarm(context, tableData)
        scheduleClassNotifications(context, tableData)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (showAddScheduleChoiceDialog) {
            RtlAlertDialog(
                onDismissRequest = { showAddScheduleChoiceDialog = false },
                title = {
                    Text(
                        "افزودن برنامه درسی",
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "روش وارد کردن برنامه درسی خود را انتخاب کنید:",
                            fontSize = 13.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        /*
                        OutlinedButton(
                            onClick = {
                                showAddScheduleChoiceDialog = false
                                showGolestanSyncDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, selectedColor),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = selectedColor.copy(alpha = 0.08f),
                                contentColor = selectedColor
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = selectedColor
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "ورود مستقیم از سایت گلستان",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        */

                        OutlinedButton(
                            onClick = {
                                showAddScheduleChoiceDialog = false
                                showPdfGuideDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, selectedColor),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = selectedColor.copy(alpha = 0.08f),
                                contentColor = selectedColor
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PictureAsPdf,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = selectedColor
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "استخراج از پی‌دی‌اف",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                showAddScheduleChoiceDialog = false
                                showFirstTimeDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "وارد کردن به صورت دستی",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAddScheduleChoiceDialog = false }) {
                        Text(AppConfig.DialogTexts.cancel, fontSize = AppConfig.dialogButtonFontSize)
                    }
                }
            )
        }

        remoteAppConfig?.let { config ->
            if (showAppUpdateDialog) {
                AppUpdateDialog(
                    config = config,
                    onDismissRequest = { showAppUpdateDialog = false }
                )
            }
        }

        if (showFirstTimeDialog) {
            RtlAlertDialog(
                onDismissRequest = { showFirstTimeDialog = false },
                title = { 
                    Text(
                        "چه روز هایی کلاس داری؟",
                        textAlign = TextAlign.Right, 
                        modifier = Modifier.fillMaxWidth(), 
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ) 
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        manualDaysList.forEach { day ->
                            val isChecked = activeDays.contains(day)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isChecked) {
                                            if (activeDays.size > 1) activeDays.remove(day)
                                        } else {
                                            activeDays.add(day)
                                            activeDays.sortWith(compareBy { fullDaysList.indexOf(it) })
                                        }
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                androidx.compose.material3.Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(day, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            prefs.edit().putString("config_visible_days", activeDays.joinToString(",")).apply()
                            if (!isSetupCompleted) {
                                scheduleMode = "golestan"
                                prefs.edit().putString("schedule_mode", "golestan").apply()
                                prefs.edit().putBoolean("is_setup_completed", true).apply()
                                isSetupCompleted = true
                                showGolestanMessageDialog = true
                            }
                            showFirstTimeDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
                    ) {
                        Text("تایید", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (showActiveHoursDialog) {
            RtlAlertDialog(
                onDismissRequest = { showActiveHoursDialog = false },
                title = { 
                    Text(
                        "انتخاب ساعات فعال جدول",
                        textAlign = TextAlign.Right, 
                        modifier = Modifier.fillMaxWidth(), 
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ) 
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        val fullHoursList = if (isHoldingMode) fullHoursHolding else fullHoursGolestan
                        val currentActiveHours = if (isHoldingMode) activeHoursHolding else activeHoursGolestan

                        fullHoursList.forEach { hour ->
                            val isChecked = currentActiveHours.contains(hour)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isChecked) {
                                            if (currentActiveHours.size > 1) currentActiveHours.remove(hour)
                                        } else {
                                            currentActiveHours.add(hour)
                                            currentActiveHours.sortWith(compareBy { fullHoursList.indexOf(it) })
                                        }
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                androidx.compose.material3.Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isHoldingMode) "$hour (زنگ)" else "ساعت $hour",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (isHoldingMode) {
                                prefs.edit().putString("config_visible_hours_holding", activeHoursHolding.joinToString(",")).apply()
                            } else {
                                prefs.edit().putString("config_visible_hours_golestan", activeHoursGolestan.joinToString(",")).apply()
                            }
                            showActiveHoursDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
                    ) {
                        Text("تایید", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (showGolestanMessageDialog) {
            RtlAlertDialog(
                onDismissRequest = { showGolestanMessageDialog = false },
                title = { 
                    Text(
                        "راهنما",
                        textAlign = TextAlign.Right, 
                        modifier = Modifier.fillMaxWidth(), 
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ) 
                },
                text = {
                    Text(
                        "مهندس، برنامه‌تو طبق برنامه‌ی گلستان بچین!",
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 15.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showGolestanMessageDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
                    ) {
                        Text("متوجه شدم", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (showClearConfirmDialog) {
            RtlAlertDialog(
                onDismissRequest = { showClearConfirmDialog = false },
                title = {
                    Text(
                        AppConfig.DialogTexts.clearConfirmTitle,
                        fontWeight = AppConfig.dialogTitleFontWeight,
                        fontSize = AppConfig.dialogTitleFontSize,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        AppConfig.DialogTexts.clearConfirmMessage,
                        fontSize = AppConfig.dialogBodyFontSize,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val editor = prefs.edit()
                            tableData.keys.toList().forEach { key ->
                                editor.remove(key)
                            }
                            editor.apply()
                            tableData.clear()
                            scheduleFoodReservationAlarm(context, tableData)
                            scheduleClassNotifications(context, tableData)
                            showClearConfirmDialog = false
                            Toast.makeText(context, "تمام دروس جدول پاک شدند", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(AppConfig.DialogTexts.clearConfirmActionButton, fontWeight = FontWeight.Bold, fontSize = AppConfig.dialogButtonFontSize)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showClearConfirmDialog = false }) {
                        Text(AppConfig.DialogTexts.cancel, fontSize = AppConfig.dialogButtonFontSize)
                    }
                }
            )
        }

        if (showTableSettingsDialog) {
            RtlAlertDialog(
                onDismissRequest = { showTableSettingsDialog = false },
                title = { 
                    Text(
                        AppConfig.DialogTexts.tableSettingsTitle, 
                        textAlign = TextAlign.Right, 
                        modifier = Modifier.fillMaxWidth(), 
                        fontWeight = AppConfig.dialogTitleFontWeight,
                        fontSize = AppConfig.dialogTitleFontSize
                    ) 
                },
                text = {
                    TableSettingsContent(
                        selectedColor = selectedColor,
                        scheduleMode = scheduleMode,
                        onScheduleModeChange = { newMode ->
                            scheduleMode = newMode
                            prefs.edit().putString("schedule_mode", newMode).apply()
                        },
                        isWeekParityFlipped = isWeekParityFlipped,
                        onWeekParityFlip = { flipped ->
                            isWeekParityFlipped = flipped
                            prefs.edit().putBoolean("isWeekParityFlipped", flipped).apply()
                        },
                        onGolestanSyncClick = {
                            showTableSettingsDialog = false
                            showGolestanSyncDialog = true
                        },
                        onPdfExtractClick = {
                            showTableSettingsDialog = false
                            showPdfGuideDialog = true
                        },
                        onClearAllClick = {
                            showTableSettingsDialog = false
                            showClearConfirmDialog = true
                        },
                        onNavigateToSchedule = {
                            showTableSettingsDialog = false
                            currentTab = 0
                        },
                        onDismissDialog = {
                            showTableSettingsDialog = false
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showTableSettingsDialog = false },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("تایید", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }



        Scaffold(
            bottomBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppConfig.bottomBarHorizontalPadding,
                            end = AppConfig.bottomBarHorizontalPadding,
                            top = 6.dp,
                            bottom = AppConfig.bottomBarBottomPadding
                        )
                        .height(AppConfig.bottomBarHeight),
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val tabs = listOf(
                            Triple(0, "برنامه", Icons.Default.CalendarToday),
                            Triple(1, "ددلاین‌ها", Icons.Default.AccessTime),
                            Triple(2, "ارتباطات", Icons.Default.Language),
                            Triple(3, "تنظیمات", Icons.Default.Settings)
                        )

                        tabs.forEach { (index, label, icon) ->
                            val isSelected = currentTab == index

                            val haloScale by animateFloatAsState(
                                targetValue = if (isSelected) 1f else 0.5f,
                                animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
                                label = "bottomBarHaloScale_$index"
                            )

                            val haloAlpha by animateFloatAsState(
                                targetValue = if (isSelected) 1f else 0f,
                                animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
                                label = "bottomBarHaloAlpha_$index"
                            )

                            val animatedTabColor by animateColorAsState(
                                targetValue = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
                                label = "bottomBarTabColor_$index"
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { if (currentTab != index) currentTab = index },
                                contentAlignment = Alignment.Center
                            ) {
                                if (haloAlpha > 0.01f) {
                                    Box(
                                        modifier = Modifier
                                            .graphicsLayer {
                                                scaleX = haloScale
                                                scaleY = haloScale
                                                alpha = haloAlpha
                                            }
                                            .widthIn(max = AppConfig.bottomBarHaloWidth)
                                            .fillMaxWidth(0.88f)
                                            .height(AppConfig.bottomBarHaloHeight)
                                            .background(
                                                color = selectedColor.copy(alpha = AppConfig.bottomBarHaloAlpha),
                                                shape = CircleShape
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = selectedColor.copy(alpha = AppConfig.bottomBarHaloAlpha * 0.7f),
                                                shape = CircleShape
                                            )
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(AppConfig.bottomBarItemSpacing, Alignment.CenterVertically)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = animatedTabColor,
                                        modifier = Modifier.size(AppConfig.bottomBarIconSize)
                                    )
                                    Text(
                                        text = label,
                                        fontSize = AppConfig.bottomBarTextSize,
                                        fontWeight = FontWeight.Bold,
                                        color = animatedTabColor,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        val duration = AppConfig.tabTransitionDurationMillis.coerceAtMost(180)
                        val isForward = targetState > initialState
                        val slideOffset = if (isForward) -48 else 48

                        (slideInHorizontally(
                            animationSpec = tween(duration, easing = FastOutSlowInEasing),
                            initialOffsetX = { slideOffset }
                        ) + fadeIn(
                            animationSpec = tween(duration, easing = FastOutSlowInEasing)
                        )).togetherWith(
                            slideOutHorizontally(
                                animationSpec = tween(duration, easing = FastOutSlowInEasing),
                                targetOffsetX = { -slideOffset }
                            ) + fadeOut(
                                animationSpec = tween(duration, easing = FastOutSlowInEasing)
                            )
                        ).using(
                            SizeTransform(clip = false)
                        )
                    },
                    label = "tabTransition",
                    modifier = Modifier.fillMaxSize()
                ) { tab ->
                    when (tab) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = 1.5.dp,
                                        color = selectedColor.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!isSetupCompleted) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable { showAddScheduleChoiceDialog = true }
                                            .padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Surface(
                                            color = selectedColor.copy(alpha = 0.12f),
                                            shape = CircleShape,
                                            modifier = Modifier
                                                .size(72.dp)
                                                .clickable { showAddScheduleChoiceDialog = true }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    Icons.Default.Add,
                                                    contentDescription = "برنامه درسی‌تو وارد کن مهندس!",
                                                    tint = selectedColor,
                                                    modifier = Modifier.size(36.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(18.dp))
                                        Text(
                                            text = "برنامه درسی‌تو وارد کن مهندس!",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = selectedColor,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                } else {
                                    ScheduleTable(
                                        tableData = effectiveTableData,
                                        selectedColor = selectedColor,
                                        visibleDays = activeDays,
                                        hourLabels = activeHours,
                                        scheduleMode = tableModeForTable,
                                        isEditingMode = (scheduleMode == "golestan"),
                                        isOverallMode = (scheduleMode == "golestan_all"),
                                        isWeekFlipped = isWeekParityFlipped,
                                        onValueChange = { key, value ->
                                            handleTableDataChange(key, value, scheduleMode, tableData, prefs)
                                            scheduleFoodReservationAlarm(context, tableData)
                                            scheduleClassNotifications(context, tableData)
                                        },
                                        onBatchValueChange = { changes ->
                                            handleBatchTableDataChange(changes, scheduleMode, tableData, prefs)
                                            scheduleFoodReservationAlarm(context, tableData)
                                            scheduleClassNotifications(context, tableData)
                                        },
                                        onAddDeadline = { courseTitle ->
                                            prefilledDeadlineCourse = courseTitle
                                            currentTab = 1
                                        },
                                        onTableSettingsRequest = {
                                            showTableSettingsDialog = true
                                        },
                                        onEditActiveDaysClick = {
                                            showFirstTimeDialog = true
                                        },
                                        onEditActiveHoursClick = {
                                            showActiveHoursDialog = true
                                        },
                                        onConfirmEditClick = {
                                            val finalIsEven = isEvenWeekContinuous(isWeekFlipped = isWeekParityFlipped)
                                            val autoHoldingMode = if (finalIsEven) "holding_even" else "holding_odd"
                                            scheduleMode = autoHoldingMode
                                            prefs.edit().putString("schedule_mode", autoHoldingMode).apply()
                                            Toast.makeText(context, "تغییرات ذخیره شد", Toast.LENGTH_SHORT).show()
                                        },
                                        onReturnToEvenOddMode = {
                                            val finalIsEven = isEvenWeekContinuous(isWeekFlipped = isWeekParityFlipped)
                                            val autoHoldingMode = if (finalIsEven) "holding_even" else "holding_odd"
                                            scheduleMode = autoHoldingMode
                                            prefs.edit().putString("schedule_mode", autoHoldingMode).apply()
                                            Toast.makeText(context, "به حالت هفته زوج و فرد بازگشتید", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                        }
                    }
                    1 -> {
                        DeadlinesScreen(
                            selectedColor = selectedColor,
                            tableData = effectiveTableData,
                            initialCourseName = prefilledDeadlineCourse,
                            onConsumedInitialCourse = { prefilledDeadlineCourse = null }
                        )
                    }
                    2 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = AppConfig.dashboardTopPadding,
                                    bottom = 16.dp
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // ==========================================
                            // ۱. کادر کادر و اساتید
                            // ==========================================
                            val isExpanded0 = expandedSitesSection == 0
                            val arrowRotation0 by animateFloatAsState(
                                targetValue = if (isExpanded0) 180f else 0f,
                                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                                label = "arrowRotation0"
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = selectedColor.copy(alpha = 0.06f),
                                border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                expandedSitesSection = if (isExpanded0) null else 0
                                            },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "کادر و اساتید",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    expandedSitesSection = 0
                                                    isStaffSearchVisible = !isStaffSearchVisible
                                                    if (!isStaffSearchVisible) {
                                                        staffSearchQuery = ""
                                                    }
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "جستجو طبق فامیل",
                                                    tint = if (isStaffSearchVisible) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = {
                                                    expandedSitesSection = if (isExpanded0) null else 0
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardArrowDown,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier
                                                        .size(22.dp)
                                                        .graphicsLayer { rotationZ = arrowRotation0 }
                                                )
                                            }
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = isExpanded0,
                                        enter = expandVertically(animationSpec = tween(280, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)),
                                        exit = shrinkVertically(animationSpec = tween(250, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clipToBounds()
                                                .padding(top = 12.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            val staffSearchFocusRequester = remember { FocusRequester() }
                                            LaunchedEffect(isStaffSearchVisible) {
                                                if (isStaffSearchVisible) {
                                                    delay(150)
                                                    staffSearchFocusRequester.requestFocus()
                                                }
                                            }

                                            AnimatedVisibility(visible = isStaffSearchVisible) {
                                                OutlinedTextField(
                                                    value = staffSearchQuery,
                                                    onValueChange = { staffSearchQuery = it },
                                                    leadingIcon = {
                                                        Icon(
                                                            imageVector = Icons.Default.Search,
                                                            contentDescription = null,
                                                            tint = selectedColor,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    },
                                                    trailingIcon = {
                                                        if (staffSearchQuery.isNotEmpty()) {
                                                            IconButton(onClick = { staffSearchQuery = "" }) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Close,
                                                                    contentDescription = "پاک کردن",
                                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                                    modifier = Modifier.size(18.dp)
                                                                )
                                                            }
                                                        }
                                                    },
                                                    singleLine = true,
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(bottom = 4.dp)
                                                        .focusRequester(staffSearchFocusRequester)
                                                )
                                            }

                                            val filteredContacts = if (staffSearchQuery.isBlank()) {
                                                contactsList
                                            } else {
                                                val q = staffSearchQuery.trim().replace("‌", "").replace(" ", "")
                                                contactsList.filter { contact ->
                                                    val nameClean = contact.name.replace("‌", "").replace(" ", "")
                                                    val titleClean = contact.title.replace("‌", "").replace(" ", "")
                                                    nameClean.contains(q, ignoreCase = true) || titleClean.contains(q, ignoreCase = true)
                                                }
                                            }

                                            if (filteredContacts.isEmpty()) {
                                                if (staffSearchQuery.isBlank()) {
                                                    Text(
                                                        text = "مخاطبی ثبت نشده است",
                                                        fontSize = 13.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        modifier = Modifier.padding(vertical = 8.dp)
                                                    )
                                                }
                                            } else {
                                                CategoryScrollBox(
                                                    itemCount = filteredContacts.size,
                                                    selectedColor = selectedColor
                                                ) {
                                                    filteredContacts.chunked(2).forEach { rowContacts ->
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                        ) {
                                                            rowContacts.forEach { contact ->
                                                                val methods = mutableListOf<Triple<String, String, Color>>()
                                                                if (contact.eitaa.isNotBlank()) methods.add(Triple("ایتا", contact.eitaa, Color(0xFFE65100)))
                                                                if (contact.telegram.isNotBlank()) methods.add(Triple("تلگرام", contact.telegram, Color(0xFF0088CC)))
                                                                if (contact.phone.isNotBlank()) methods.add(Triple("شماره تماس", contact.phone, Color(0xFF2E7D32)))
                                                                if (contact.email.isNotBlank()) methods.add(Triple("ایمیل", contact.email, Color(0xFFEA4335)))

                                                                if (activeContactChooser == contact) {
                                                                    Column(
                                                                        modifier = Modifier
                                                                            .weight(1f)
                                                                            .height(65.dp),
                                                                        verticalArrangement = Arrangement.SpaceBetween
                                                                    ) {
                                                                        when (methods.size) {
                                                                            1 -> {
                                                                                val (label, value, color) = methods[0]
                                                                                ThreeDButton(
                                                                                    onClick = {
                                                                                        activeContactChooser = null
                                                                                        openContactLink(context, label, value)
                                                                                    },
                                                                                    baseColor = color,
                                                                                    modifier = Modifier
                                                                                        .fillMaxWidth()
                                                                                        .height(65.dp)
                                                                                ) {
                                                                                    Box(
                                                                                        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                                                                                        contentAlignment = Alignment.Center
                                                                                    ) {
                                                                                        Text(
                                                                                            text = label,
                                                                                            fontSize = 14.5.sp,
                                                                                            fontWeight = FontWeight.Bold,
                                                                                            color = Color.White,
                                                                                            maxLines = 1,
                                                                                            textAlign = TextAlign.Center
                                                                                        )
                                                                                    }
                                                                                }
                                                                            }
                                                                            2 -> {
                                                                                Row(
                                                                                    modifier = Modifier.fillMaxWidth().height(65.dp),
                                                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                                                ) {
                                                                                    methods.forEach { (label, value, color) ->
                                                                                        ThreeDButton(
                                                                                            onClick = {
                                                                                                activeContactChooser = null
                                                                                                openContactLink(context, label, value)
                                                                                            },
                                                                                            baseColor = color,
                                                                                            modifier = Modifier
                                                                                                .weight(1f)
                                                                                                .fillMaxHeight()
                                                                                        ) {
                                                                                            Box(
                                                                                                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                                                                                                contentAlignment = Alignment.Center
                                                                                            ) {
                                                                                                Text(
                                                                                                    text = label,
                                                                                                    fontSize = 14.sp,
                                                                                                    fontWeight = FontWeight.Bold,
                                                                                                    color = Color.White,
                                                                                                    maxLines = 1,
                                                                                                    textAlign = TextAlign.Center
                                                                                                )
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            3 -> {
                                                                                Row(
                                                                                    modifier = Modifier.fillMaxWidth().height(31.dp),
                                                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                                                ) {
                                                                                    for (i in 0..1) {
                                                                                        val (label, value, color) = methods[i]
                                                                                        ThreeDButton(
                                                                                            onClick = {
                                                                                                activeContactChooser = null
                                                                                                openContactLink(context, label, value)
                                                                                            },
                                                                                            baseColor = color,
                                                                                            modifier = Modifier
                                                                                                .weight(1f)
                                                                                                .fillMaxHeight()
                                                                                        ) {
                                                                                            Box(
                                                                                                modifier = Modifier.fillMaxSize().padding(horizontal = 2.dp),
                                                                                                contentAlignment = Alignment.Center
                                                                                            ) {
                                                                                                Text(
                                                                                                    text = label,
                                                                                                    fontSize = 11.sp,
                                                                                                    fontWeight = FontWeight.Bold,
                                                                                                    color = Color.White,
                                                                                                    maxLines = 1,
                                                                                                    textAlign = TextAlign.Center
                                                                                                )
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                val (label2, value2, color2) = methods[2]
                                                                                ThreeDButton(
                                                                                    onClick = {
                                                                                        activeContactChooser = null
                                                                                        openContactLink(context, label2, value2)
                                                                                    },
                                                                                    baseColor = color2,
                                                                                    modifier = Modifier
                                                                                        .fillMaxWidth()
                                                                                        .height(31.dp)
                                                                                ) {
                                                                                    Box(
                                                                                        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                                                                                        contentAlignment = Alignment.Center
                                                                                    ) {
                                                                                        Text(
                                                                                            text = label2,
                                                                                            fontSize = 11.sp,
                                                                                            fontWeight = FontWeight.Bold,
                                                                                            color = Color.White,
                                                                                            maxLines = 1,
                                                                                            textAlign = TextAlign.Center
                                                                                        )
                                                                                    }
                                                                                }
                                                                            }
                                                                            else -> {
                                                                                methods.chunked(2).forEach { rowMethods ->
                                                                                    Row(
                                                                                        modifier = Modifier.fillMaxWidth().height(31.dp),
                                                                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                                                    ) {
                                                                                        rowMethods.forEach { (label, value, color) ->
                                                                                            ThreeDButton(
                                                                                                onClick = {
                                                                                                    activeContactChooser = null
                                                                                                    openContactLink(context, label, value)
                                                                                                },
                                                                                                baseColor = color,
                                                                                                modifier = Modifier
                                                                                                    .weight(1f)
                                                                                                    .fillMaxHeight()
                                                                                            ) {
                                                                                                Box(
                                                                                                    modifier = Modifier.fillMaxSize().padding(horizontal = 2.dp),
                                                                                                    contentAlignment = Alignment.Center
                                                                                                ) {
                                                                                                    Text(
                                                                                                        text = label,
                                                                                                        fontSize = 11.sp,
                                                                                                        fontWeight = FontWeight.Bold,
                                                                                                        color = Color.White,
                                                                                                        maxLines = 1,
                                                                                                        textAlign = TextAlign.Center
                                                                                                    )
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if (rowMethods.size == 1) {
                                                                                            Spacer(modifier = Modifier.weight(1f))
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    ThreeDButton(
                                                                        onClick = {
                                                                            if (methods.isEmpty()) {
                                                                                Toast.makeText(context, "اطلاعات تماسی ثبت نشده است", Toast.LENGTH_SHORT).show()
                                                                            } else {
                                                                                activeContactChooser = if (activeContactChooser == contact) null else contact
                                                                            }
                                                                        },
                                                                        baseColor = selectedColor,
                                                                        modifier = Modifier
                                                                            .weight(1f)
                                                                            .height(65.dp)
                                                                    ) {
                                                                        Box(
                                                                            modifier = Modifier
                                                                                .fillMaxSize()
                                                                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                                                        ) {
                                                                            Text(
                                                                                text = contact.title,
                                                                                fontSize = AppConfig.contactTitleFontSize,
                                                                                fontWeight = FontWeight.Medium,
                                                                                color = Color.White,
                                                                                textAlign = TextAlign.Center,
                                                                                maxLines = 1,
                                                                                modifier = Modifier
                                                                                    .fillMaxWidth()
                                                                                    .align(Alignment.TopCenter)
                                                                            )
                                                                            Text(
                                                                                text = contact.name.replace("حجه السلام", "حجة‌الاسلام").replace("حجه الاسلام", "حجة‌الاسلام"),
                                                                                fontSize = AppConfig.contactNameFontSize,
                                                                                fontWeight = FontWeight.Bold,
                                                                                color = Color.White,
                                                                                textAlign = TextAlign.Center,
                                                                                maxLines = 1,
                                                                                modifier = Modifier
                                                                                    .fillMaxWidth()
                                                                                    .align(Alignment.Center)
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if (rowContacts.size == 1) {
                                                                Spacer(modifier = Modifier.weight(1f))
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==========================================
                            // ۱.۲. کادر تلفن‌های ثابت
                            // ==========================================
                            val isExpandedFixedPhones = expandedSitesSection == 5
                            val arrowRotationFixedPhones by animateFloatAsState(
                                targetValue = if (isExpandedFixedPhones) 180f else 0f,
                                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                                label = "arrowRotationFixedPhones"
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = selectedColor.copy(alpha = 0.06f),
                                border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                expandedSitesSection = if (isExpandedFixedPhones) null else 5
                                            },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "تلفن‌های ثابت دانشگاه",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.graphicsLayer { rotationZ = arrowRotationFixedPhones }
                                        )
                                    }

                                    AnimatedVisibility(
                                        visible = isExpandedFixedPhones,
                                        enter = expandVertically(animationSpec = tween(280, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)),
                                        exit = shrinkVertically(animationSpec = tween(250, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))
                                    ) {
                                        val fixedPhones = listOf(
                                            Triple("تلفن ۱", "03153302500", "phone"),
                                            Triple("تلفن ۲", "03153302499", "phone"),
                                            Triple("تلفن ۳", "03153302498", "phone"),
                                            Triple("تلفن ۴", "03153302497", "phone"),
                                            Triple("تلفن ۵", "03153238379", "phone"),
                                            Triple("کدهای داخلی", "phone_numbers.pdf", "pdf"),
                                            Triple("خوابگاه خانم‌ها", "03153247007", "phone"),
                                            Triple("خوابگاه آقایان", "03153245013", "phone")
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clipToBounds()
                                                .padding(top = 12.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            CategoryScrollBox(
                                                itemCount = fixedPhones.size,
                                                selectedColor = selectedColor,
                                                maxRows = 3
                                            ) {
                                                fixedPhones.chunked(2).forEach { rowPhones ->
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                    ) {
                                                        rowPhones.forEach { (title, detail, type) ->
                                                            ThreeDButton(
                                                                onClick = {
                                                                    if (type == "pdf") {
                                                                        openPdfFromAssets(context, detail)
                                                                    } else if (detail.isNotBlank()) {
                                                                        try {
                                                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$detail"))
                                                                            context.startActivity(intent)
                                                                        } catch (e: Exception) {
                                                                            Toast.makeText(context, "امکان تماس وجود ندارد", Toast.LENGTH_SHORT).show()
                                                                        }
                                                                    }
                                                                },
                                                                baseColor = selectedColor,
                                                                modifier = Modifier
                                                                    .weight(1f)
                                                                    .height(65.dp)
                                                            ) {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .fillMaxSize()
                                                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Text(
                                                                        text = title,
                                                                        fontSize = 14.5.sp,
                                                                        fontWeight = FontWeight.Bold,
                                                                        color = Color.White,
                                                                        textAlign = TextAlign.Center,
                                                                        maxLines = 2
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        if (rowPhones.size == 1) {
                                                            Spacer(modifier = Modifier.weight(1f))
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==========================================
                            // ۱.۵. کادر مکان‌ها
                            // ==========================================
                            val isExpanded1 = expandedSitesSection == 1
                            val arrowRotation1 by animateFloatAsState(
                                targetValue = if (isExpanded1) 180f else 0f,
                                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                                label = "arrowRotation1"
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = selectedColor.copy(alpha = 0.06f),
                                border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                expandedSitesSection = if (isExpanded1) null else 1
                                            },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "مکان‌ها",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.graphicsLayer { rotationZ = arrowRotation1 }
                                        )
                                    }

                                    AnimatedVisibility(
                                        visible = isExpanded1,
                                        enter = expandVertically(animationSpec = tween(280, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)),
                                        exit = shrinkVertically(animationSpec = tween(250, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))
                                    ) {
                                        val locationItems = listOf(
                                            Triple("دانشگاه", AppConfig.universityLat, AppConfig.universityLng),
                                            Triple("خوابگاه آقایان", AppConfig.mensDormLat, AppConfig.mensDormLng),
                                            Triple("خوابگاه خانم‌ها", AppConfig.womensDormLat, AppConfig.womensDormLng)
                                        )

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clipToBounds()
                                                .padding(top = 12.dp),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            locationItems.forEach { (title, lat, lng) ->
                                                ThreeDButton(
                                                    onClick = {
                                                        openLocationInMap(context, lat, lng, title)
                                                    },
                                                    baseColor = selectedColor,
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(65.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(horizontal = 4.dp, vertical = 2.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = title,
                                                            fontSize = 14.5.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color.White,
                                                            textAlign = TextAlign.Center,
                                                            maxLines = 2
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==========================================
                            // ۲. کادر کانال‌ها و گروه‌ها
                            // ==========================================
                            val isExpanded2 = expandedSitesSection == 2
                            val arrowRotation2 by animateFloatAsState(
                                targetValue = if (isExpanded2) 180f else 0f,
                                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                                label = "arrowRotation2"
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = selectedColor.copy(alpha = 0.06f),
                                border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                expandedSitesSection = if (isExpanded2) null else 2
                                            },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "کانال‌ها و گروه‌ها",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {

                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.graphicsLayer { rotationZ = arrowRotation2 }
                                            )
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = isExpanded2,
                                        enter = expandVertically(animationSpec = tween(280, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)),
                                        exit = shrinkVertically(animationSpec = tween(250, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clipToBounds()
                                                .padding(top = 12.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            telegramChannelsList.chunked(2).forEach { rowChannels ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    rowChannels.forEach { channel ->
                                                        if (activeChannelChooser == channel) {
                                                            Row(
                                                                modifier = Modifier
                                                                    .weight(1f)
                                                                    .height(65.dp),
                                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                ThreeDButton(
                                                                    onClick = {
                                                                        activeChannelChooser = null
                                                                        openTelegramLink(context, channel.url)
                                                                    },
                                                                    baseColor = Color(0xFF0088CC),
                                                                    modifier = Modifier
                                                                        .weight(1f)
                                                                        .fillMaxHeight()
                                                                ) {
                                                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                                        Text(
                                                                            text = "تلگرام",
                                                                            fontSize = 13.sp,
                                                                            fontWeight = FontWeight.Bold,
                                                                            color = Color.White,
                                                                            maxLines = 1,
                                                                            textAlign = TextAlign.Center
                                                                        )
                                                                    }
                                                                }

                                                                ThreeDButton(
                                                                    onClick = {
                                                                        activeChannelChooser = null
                                                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channel.eitaaUrl))
                                                                        context.startActivity(intent)
                                                                    },
                                                                    baseColor = Color(0xFFE65100),
                                                                    modifier = Modifier
                                                                        .weight(1f)
                                                                        .fillMaxHeight()
                                                                ) {
                                                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                                        Text(
                                                                            text = "ایتا",
                                                                            fontSize = 13.sp,
                                                                            fontWeight = FontWeight.Bold,
                                                                            color = Color.White,
                                                                            maxLines = 1,
                                                                            textAlign = TextAlign.Center
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            ThreeDButton(
                                                                onClick = {
                                                                    if (channel.eitaaUrl.isNotBlank()) {
                                                                        activeChannelChooser = if (activeChannelChooser == channel) null else channel
                                                                    } else {
                                                                        openTelegramLink(context, channel.url)
                                                                    }
                                                                },
                                                                baseColor = selectedColor,
                                                                modifier = Modifier
                                                                    .weight(1f)
                                                                    .height(65.dp)
                                                            ) {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .fillMaxSize()
                                                                        .padding(horizontal = 6.dp),
                                                                    contentAlignment = Alignment.Center
                                                                ) {


                                                                    Text(
                                                                        text = channel.name,
                                                                        fontSize = 16.5.sp,
                                                                        fontWeight = FontWeight.Bold,
                                                                        color = Color.White,
                                                                        maxLines = 1,
                                                                        textAlign = TextAlign.Center
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (rowChannels.size == 1) {
                                                        Spacer(modifier = Modifier.weight(1f))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==========================================
                            // ۳. کادر گزارش‌های گلستان
                            // ==========================================
                            val isExpandedGolestan = expandedSitesSection == 3
                            val arrowRotationGolestan by animateFloatAsState(
                                targetValue = if (isExpandedGolestan) 180f else 0f,
                                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                                label = "arrowRotationGolestan"
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = selectedColor.copy(alpha = 0.06f),
                                border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                expandedSitesSection = if (isExpandedGolestan) null else 3
                                            },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "گزارش‌های گلستان",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    expandedSitesSection = 3
                                                    isGolestanSearchVisible = !isGolestanSearchVisible
                                                    if (!isGolestanSearchVisible) {
                                                        golestanSearchQuery = ""
                                                    }
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "جستجوی گزارش",
                                                    tint = if (isGolestanSearchVisible) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = {
                                                    expandedSitesSection = if (isExpandedGolestan) null else 3
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardArrowDown,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier
                                                        .size(22.dp)
                                                        .graphicsLayer { rotationZ = arrowRotationGolestan }
                                                )
                                            }
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = isExpandedGolestan,
                                        enter = expandVertically(animationSpec = tween(280, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)),
                                        exit = shrinkVertically(animationSpec = tween(250, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))
                                    ) {
                                        val golestanReports = listOf(
                                            Pair("برنامه‌ی امتحانات پایان‌ترم", "428"),
                                            Pair("برنامه هفتگی جهت امتحان", "73"),
                                            Pair("تغییرات انتخاب واحد", "74"),
                                            Pair("تاییدیه‌ی انتخاب واحد", "515"),
                                            Pair("گزارش تطبیق واحد‌ها", "284"),
                                            Pair("برنامه پیشنهادی و هم‌نیاز", "1271"),
                                            Pair("معافیت تحصیلی", "5163"),
                                            Pair("گواهی اشتغال به تحصیل", "1251"),
                                            Pair("کارنامه مالی دانشجو", "1110"),
                                            Pair("برنامه هفتگی ۱", "78"),
                                            Pair("برنامه هفتگی ۲", "88"),
                                            Pair("برنامه هفتگی ۳", "1857"),
                                            Pair("درس‌های ارائه شده ۱", "211"),
                                            Pair("درس‌های ارائه شده ۲", "212"),
                                            Pair("کارنامه‌ی ترمی ۱", "79"),
                                            Pair("کارنامه‌ی ترمی ۲", "439")
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clipToBounds()
                                                .padding(top = 12.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            val golestanSearchFocusRequester = remember { FocusRequester() }
                                            LaunchedEffect(isGolestanSearchVisible) {
                                                if (isGolestanSearchVisible) {
                                                    delay(150)
                                                    golestanSearchFocusRequester.requestFocus()
                                                }
                                            }

                                            AnimatedVisibility(visible = isGolestanSearchVisible) {
                                                OutlinedTextField(
                                                    value = golestanSearchQuery,
                                                    onValueChange = { golestanSearchQuery = it },
                                                    leadingIcon = {
                                                        Icon(
                                                            imageVector = Icons.Default.Search,
                                                            contentDescription = null,
                                                            tint = selectedColor,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    },
                                                    trailingIcon = {
                                                        if (golestanSearchQuery.isNotEmpty()) {
                                                            IconButton(onClick = { golestanSearchQuery = "" }) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Close,
                                                                    contentDescription = "پاک کردن",
                                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                                    modifier = Modifier.size(18.dp)
                                                                )
                                                            }
                                                        }
                                                    },
                                                    singleLine = true,
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(bottom = 4.dp)
                                                        .focusRequester(golestanSearchFocusRequester)
                                                )
                                            }

                                            val filteredReports = if (golestanSearchQuery.isBlank()) {
                                                golestanReports
                                            } else {
                                                val q = golestanSearchQuery.trim().replace("‌", "").replace(" ", "")
                                                golestanReports.filter { (repTitle, repCode) ->
                                                    val titleClean = repTitle.replace("‌", "").replace(" ", "")
                                                    titleClean.contains(q, ignoreCase = true) || repCode.contains(q)
                                                }
                                            }

                                            if (!filteredReports.isEmpty()) {
                                                CategoryScrollBox(
                                                    itemCount = filteredReports.size,
                                                    selectedColor = selectedColor
                                                ) {
                                                    filteredReports.chunked(2).forEach { rowReports ->
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                        ) {
                                                            rowReports.forEach { (repTitle, repCode) ->
                                                                var buttonText by remember(repTitle, repCode) { mutableStateOf(repTitle) }
                                                                val coroutineScope = rememberCoroutineScope()

                                                                ThreeDButton(
                                                                    onClick = {
                                                                        if (repCode == "main") {
                                                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://golestan.ui.ac.ir/forms/authenticateuser/main.htm"))
                                                                            context.startActivity(intent)
                                                                        } else {
                                                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                                            val clip = ClipData.newPlainText(repTitle, repCode)
                                                                            clipboard.setPrimaryClip(clip)
                                                                            buttonText = repCode
                                                                            coroutineScope.launch {
                                                                                delay(2000)
                                                                                buttonText = repTitle
                                                                            }
                                                                        }
                                                                    },
                                                                    baseColor = selectedColor,
                                                                    modifier = Modifier
                                                                        .weight(1f)
                                                                        .height(65.dp)
                                                                ) {
                                                                    Box(
                                                                        modifier = Modifier
                                                                            .fillMaxSize()
                                                                            .padding(horizontal = 4.dp, vertical = 2.dp),
                                                                        contentAlignment = Alignment.Center
                                                                    ) {
                                                                        Text(
                                                                            text = buttonText,
                                                                            fontSize = 12.sp,
                                                                            fontWeight = FontWeight.Bold,
                                                                            color = Color.White,
                                                                            textAlign = TextAlign.Center,
                                                                            maxLines = 2,
                                                                            modifier = Modifier.fillMaxWidth()
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                            if (rowReports.size == 1) {
                                                                Spacer(modifier = Modifier.weight(1f))
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ==========================================
                            // ۴. کادر سامانه‌های دانشگاه
                            // ==========================================
                            val isExpanded4 = expandedSitesSection == 4
                            val arrowRotation4 by animateFloatAsState(
                                targetValue = if (isExpanded4) 180f else 0f,
                                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                                label = "arrowRotation4"
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = selectedColor.copy(alpha = 0.06f),
                                border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                expandedSitesSection = if (isExpanded4) null else 4
                                            },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "سامانه‌های دانشگاه",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {

                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.graphicsLayer { rotationZ = arrowRotation4 }
                                            )
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = isExpanded4,
                                        enter = expandVertically(animationSpec = tween(280, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)),
                                        exit = shrinkVertically(animationSpec = tween(250, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clipToBounds()
                                                .padding(top = 12.dp)
                                        ) {
                                            CategoryScrollBox(
                                                itemCount = sitesList.size,
                                                selectedColor = selectedColor
                                            ) {
                                                sitesList.chunked(2).forEach { rowSites ->
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                    ) {
                                                        rowSites.forEach { site ->
                                                            ThreeDButton(
                                                                onClick = {
                                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(site.url))
                                                                    context.startActivity(intent)
                                                                },
                                                                baseColor = selectedColor,
                                                                modifier = Modifier
                                                                    .weight(1f)
                                                                    .height(65.dp)
                                                                ) {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .fillMaxSize()
                                                                        .padding(horizontal = 6.dp),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Text(
                                                                        text = site.name,
                                                                        fontSize = 14.5.sp,
                                                                        fontWeight = FontWeight.Bold,
                                                                        color = Color.White,
                                                                        maxLines = 1,
                                                                        textAlign = TextAlign.Center
                                                                    )
                                                                }
                                                            }
                                                        }
                                                        if (rowSites.size == 1) {
                                                            Spacer(modifier = Modifier.weight(1f))
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                    3 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            NotificationSettingsCard(
                                isClassNotificationEnabled = isClassNotificationEnabled,
                                onClassNotificationChange = { newValue ->
                                    isClassNotificationEnabled = newValue
                                    prefs.edit().putBoolean("pref_class_notification_enabled", newValue).apply()
                                    if (newValue && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                    scheduleClassNotifications(context, tableData)
                                },
                                isFoodNotificationEnabled = isFoodNotificationEnabled,
                                onFoodNotificationChange = { newValue ->
                                    isFoodNotificationEnabled = newValue
                                    prefs.edit().putBoolean("pref_food_notification_enabled", newValue).apply()
                                    if (newValue && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                    scheduleFoodReservationAlarm(context, tableData)
                                },
                                foodNotificationDay = foodNotificationDay,
                                onFoodNotificationDayChange = { newDay ->
                                    foodNotificationDay = newDay
                                    prefs.edit().putString("pref_food_notification_day", newDay).apply()
                                    scheduleFoodReservationAlarm(context, tableData)
                                },
                                foodNotificationHour = foodNotificationHour,
                                foodNotificationMinute = foodNotificationMinute,
                                onFoodNotificationTimeChange = { hour, minute ->
                                    foodNotificationHour = hour
                                    foodNotificationMinute = minute
                                    prefs.edit()
                                        .putInt("pref_food_notification_hour", hour)
                                        .putInt("pref_food_notification_minute", minute)
                                        .apply()
                                    scheduleFoodReservationAlarm(context, tableData)
                                },
                                selectedColor = selectedColor
                            )

                            // کادر ظاهر برنامه
                            var showDarkModePickerDialog by remember { mutableStateOf(false) }
                            val displayDarkModeText = when (darkModeMode) {
                                "dark" -> "دارک"
                                "system" -> "طبق سیستم"
                                else -> "لایت"
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = selectedColor.copy(alpha = 0.06f),
                                border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "ظاهر برنامه",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { showDarkModePickerDialog = true }
                                            .padding(vertical = 2.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("حالت تم", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = selectedColor.copy(alpha = 0.15f),
                                            border = BorderStroke(1.dp, selectedColor.copy(alpha = 0.4f))
                                        ) {
                                            Text(
                                                text = displayDarkModeText,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = selectedColor,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    if (showDarkModePickerDialog) {
                                        CustomDarkModePickerDialog(
                                            initialMode = darkModeMode,
                                            selectedColor = selectedColor,
                                            onDismiss = { showDarkModePickerDialog = false },
                                            onConfirm = { newMode ->
                                                onDarkModeModeChange(newMode)
                                                showDarkModePickerDialog = false
                                            }
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text("رنگ تم", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            rainbowColors.forEach { color ->
                                                Box(
                                                    modifier = Modifier
                                                        .clickable(
                                                            interactionSource = remember { MutableInteractionSource() },
                                                            indication = null
                                                        ) { onColorChange(color) },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(26.dp)
                                                            .clip(CircleShape)
                                                            .background(color)
                                                            .then(
                                                                if (selectedColor == color) {
                                                                    Modifier.border(
                                                                        width = 2.dp,
                                                                        color = MaterialTheme.colorScheme.onSurface,
                                                                        shape = CircleShape
                                                                    )
                                                                } else Modifier
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        if (selectedColor == color) {
                                                            Icon(
                                                                imageVector = Icons.Default.Check,
                                                                contentDescription = null,
                                                                tint = Color.White,
                                                                modifier = Modifier.size(13.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // کادر تنظیمات جدول درسی
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = selectedColor.copy(alpha = 0.06f),
                                border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = AppConfig.DialogTexts.tableSettingsTitle,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    TableSettingsContent(
                                        selectedColor = selectedColor,
                                        scheduleMode = scheduleMode,
                                        onScheduleModeChange = { newMode ->
                                            scheduleMode = newMode
                                            prefs.edit().putString("schedule_mode", newMode).apply()
                                        },
                                        isWeekParityFlipped = isWeekParityFlipped,
                                        onWeekParityFlip = { flipped ->
                                            isWeekParityFlipped = flipped
                                            prefs.edit().putBoolean("isWeekParityFlipped", flipped).apply()
                                        },
                                        onGolestanSyncClick = {
                                            showGolestanSyncDialog = true
                                            currentTab = 0
                                        },
                                        onPdfExtractClick = {
                                            showPdfGuideDialog = true
                                        },
                                        onClearAllClick = {
                                            showClearConfirmDialog = true
                                        },
                                        onNavigateToSchedule = {
                                            currentTab = 0
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
              }
            }
        }

        if (activeDetailDialog != null) {
            val details = activeDetailDialog!!
            RtlAlertDialog(
                onDismissRequest = { activeDetailDialog = null },
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = details.contact.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center,
                            color = selectedColor
                        )
                        Text(
                            text = details.contact.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = selectedColor.copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, selectedColor.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = details.selectedType ?: "",
                                    fontSize = 12.sp,
                                    color = selectedColor,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = details.selectedValue ?: "",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "اطلاعات در حافظه (کلیپ‌بورد) کپی شد",
                                fontSize = 12.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!details.openUrl.isNullOrBlank()) {
                            Button(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(details.openUrl))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "امکان باز کردن برنامه وجود ندارد", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
                            ) {
                                Text(if (details.selectedType == "شماره تماس") "تماس" else "بازکردن")
                            }
                        }
                        TextButton(onClick = { activeDetailDialog = null }) {
                            Text("بستن")
                        }
                    }
                }
            )
        }



        if (showAddContactDialog || editingContact != null) {
            var titleInput by remember { mutableStateOf(editingContact?.title ?: "") }
            var nameInput by remember { mutableStateOf(editingContact?.name ?: "") }
            var phoneInput by remember { mutableStateOf(editingContact?.phone ?: "") }
            var telegramInput by remember { mutableStateOf(editingContact?.telegram ?: "") }
            var eitaaInput by remember { mutableStateOf(editingContact?.eitaa ?: "") }
            var emailInput by remember { mutableStateOf(editingContact?.email ?: "") }

            RtlAlertDialog(
                onDismissRequest = {
                    showAddContactDialog = false
                    editingContact = null
                },
                title = {
                    Text(
                        text = if (editingContact != null) "ویرایش مخاطب" else "افزودن مخاطب جدید",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = selectedColor
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = titleInput,
                            onValueChange = { titleInput = it },
                            label = { Text("سمت / عنوان (مثلاً امور دانشجویی)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("نام و فامیل (مثلاً آقای رمضانی)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = eitaaInput,
                            onValueChange = { eitaaInput = it },
                            label = { Text("آیدی ایتا (مثلاً iman2015ra@)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = telegramInput,
                            onValueChange = { telegramInput = it },
                            label = { Text("آیدی تلگرام (مثلاً public_shrun@)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = { phoneInput = it },
                            label = { Text("شماره تماس (مثلاً 09382747886)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("ایمیل (مثلاً mech.shr@gmail.com)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (titleInput.isBlank() || nameInput.isBlank()) {
                                Toast.makeText(context, "لطفاً سمت و نام را وارد کنید", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val newContact = ContactInfo(
                                id = editingContact?.id ?: java.util.UUID.randomUUID().toString(),
                                title = titleInput.trim(),
                                name = nameInput.trim(),
                                phone = phoneInput.trim(),
                                telegram = telegramInput.trim(),
                                eitaa = eitaaInput.trim(),
                                email = emailInput.trim()
                            )
                            val newList = contactsList.toMutableList()
                            if (editingContact != null) {
                                val index = newList.indexOfFirst { it.id == editingContact!!.id }
                                if (index != -1) {
                                    newList[index] = newContact
                                }
                            } else {
                                newList.add(newContact)
                            }
                            contactsList = newList
                            saveContacts(context, newList)

                            showAddContactDialog = false
                            editingContact = null
                            Toast.makeText(context, "مخاطب با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
                    ) {
                        Text("ذخیره")
                    }
                },
                dismissButton = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (editingContact != null) {
                            TextButton(
                                onClick = {
                                    val contactIdToDelete = editingContact!!.id
                                    val newList = contactsList.filterNot { it.id == contactIdToDelete }
                                    contactsList = newList
                                    saveContacts(context, newList)

                                    showAddContactDialog = false
                                    editingContact = null
                                    Toast.makeText(context, "مخاطب حذف شد", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text("حذف", color = MaterialTheme.colorScheme.error)
                            }
                        }
                        TextButton(
                            onClick = {
                                showAddContactDialog = false
                                editingContact = null
                            }
                        ) {
                            Text("لغو")
                        }
                    }
                }
            )
        }



        // دیالوگ نمایش اعلان همگانی دریافت شده از مدیریت
        if (showAnnouncementDialog) {
            RtlAlertDialog(
                onDismissRequest = { showAnnouncementDialog = false },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = selectedColor)
                        Text(
                            text = announcementDialogTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = selectedColor
                        )
                    }
                },
                text = {
                    Text(
                        text = announcementDialogMessage,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showAnnouncementDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = selectedColor)
                    ) {
                        Text("متوجه شدم")
                    }
                }
            )
        }

        if (showGolestanSyncDialog) {
            GolestanSyncDialog(
                themeColor = selectedColor,
                onCoursesExtracted = { courses ->
                    showGolestanSyncDialog = false
                    parsedCourses = courses
                    showParsedCoursesDialog = true
                },
                onDismiss = { showGolestanSyncDialog = false }
            )
        }

        if (showPdfGuideDialog) {
            PdfExtractionGuideDialog(
                onDismiss = { showPdfGuideDialog = false },
                onSelectPdf = {
                    showPdfGuideDialog = false
                    pdfPickerLauncher.launch("application/pdf")
                },
                selectedColor = selectedColor
            )
        }

        if (showParsedCoursesDialog && parsedCourses != null) {
            ParsedCoursesDialog(
                initialCourses = parsedCourses!!,
                themeColor = selectedColor,
                onApplyCourses = { selected ->
                    showParsedCoursesDialog = false
                    importDetectedCourses(selected)
                },
                onDismiss = { showParsedCoursesDialog = false }
            )
        }

        if (isExtractingPdf) {
            PdfLoadingDialog(themeColor = selectedColor)
        }
    }
}

@Composable
private fun SettingsOptionButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(10.dp),
        color = Color.Transparent,
        border = BorderStroke(1.5.dp, color.copy(alpha = 0.7f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                maxLines = 1
            )
        }
    }
}

@Composable
fun TableSettingsContent(
    selectedColor: Color,
    scheduleMode: String,
    onScheduleModeChange: (String) -> Unit,
    isWeekParityFlipped: Boolean,
    onWeekParityFlip: (Boolean) -> Unit,
    onGolestanSyncClick: () -> Unit = {},
    onPdfExtractClick: () -> Unit,
    onClearAllClick: () -> Unit,
    onNavigateToSchedule: () -> Unit = {},
    onDismissDialog: () -> Unit = {}
) {
    val context = LocalContext.current
    val isEditingMode = scheduleMode == "golestan"
    val isOverallMode = scheduleMode == "golestan_all"

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ۱. مشاهده برنامه کلی / برگشت به حالت زوج و فرد
        SettingsOptionButton(
            text = if (isOverallMode) "برگشت به حالت زوج و فرد" else "مشاهده برنامه کلی",
            icon = if (isOverallMode) Icons.Default.Refresh else Icons.Default.CalendarToday,
            color = selectedColor,
            onClick = {
                if (isOverallMode) {
                    val finalIsEven = isEvenWeekContinuous(isWeekFlipped = isWeekParityFlipped)
                    val autoHoldingMode = if (finalIsEven) "holding_even" else "holding_odd"
                    onScheduleModeChange(autoHoldingMode)
                    Toast.makeText(context, "به حالت هفته زوج و فرد بازگشتید", Toast.LENGTH_SHORT).show()
                } else {
                    onScheduleModeChange("golestan_all")
                    Toast.makeText(context, "در حال مشاهده برنامه کلی", Toast.LENGTH_SHORT).show()
                }
                onNavigateToSchedule()
                onDismissDialog()
            }
        )

        // ۲. ورود به حالت ویرایش / ثبت و خروج از حالت ویرایش
        SettingsOptionButton(
            text = if (isEditingMode) "ثبت و خروج از حالت ویرایش" else "ورود به حالت ویرایش",
            icon = if (isEditingMode) Icons.Default.Check else Icons.Default.Edit,
            color = MaterialTheme.colorScheme.onSurface,
            onClick = {
                if (isEditingMode) {
                    val finalIsEven = isEvenWeekContinuous(isWeekFlipped = isWeekParityFlipped)
                    val autoHoldingMode = if (finalIsEven) "holding_even" else "holding_odd"
                    onScheduleModeChange(autoHoldingMode)
                    Toast.makeText(context, "تغییرات ذخیره شد و به حالت مشاهده بازگشتید", Toast.LENGTH_SHORT).show()
                    onNavigateToSchedule()
                    onDismissDialog()
                } else {
                    onScheduleModeChange("golestan")
                    Toast.makeText(context, "وارد حالت ویرایش شدید", Toast.LENGTH_SHORT).show()
                    onNavigateToSchedule()
                    onDismissDialog()
                }
            }
        )

        // ۳. استخراج برنامه از فایل PDF
        SettingsOptionButton(
            text = AppConfig.DialogTexts.pdfExtractButton,
            icon = Icons.Default.PictureAsPdf,
            color = MaterialTheme.colorScheme.onSurface,
            onClick = {
                onPdfExtractClick()
                onNavigateToSchedule()
            }
        )

        // ۴. حذف همه دروس جدول
        SettingsOptionButton(
            text = AppConfig.DialogTexts.clearAllCoursesButton,
            icon = Icons.Default.Delete,
            color = MaterialTheme.colorScheme.error,
            onClick = {
                onClearAllClick()
                onNavigateToSchedule()
            }
        )
    }
}

fun sendFoodReservationNotification(context: Context) {
    val channelId = "food_reservation_channel"
    val notificationId = 1001

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "یادآوری رزرو غذا",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "اطلاع‌رسانی‌های مرتبط با رزرو غذای کالینان"
        }
        notificationManager.createNotificationChannel(channel)
    }

    val calinanUrl = "https://shrfood.ui.ac.ir/Reservation/Reservation.aspx"
    val calinanIntent = Intent(Intent.ACTION_VIEW, Uri.parse(calinanUrl)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        calinanIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("مهندس یادت نره غذا رزرو کنی!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .addAction(
            0,
            "رفتن به کالینان",
            pendingIntent
        )

    notificationManager.notify(notificationId, builder.build())
}

class FoodAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("pref_food_notification_enabled", true)
        if (isEnabled) {
            sendFoodReservationNotification(context)
        }
        val tableData = mutableMapOf<String, String>()
        prefs.all.forEach { (key, value) ->
            if ((key.startsWith("block_") || key.startsWith("duration_") || key.startsWith("room_")) && value is String) {
                tableData[key] = value
            }
        }
        scheduleFoodReservationAlarm(context, tableData)
        scheduleClassNotifications(context, tableData)
    }
}

fun scheduleFoodReservationAlarm(context: Context, tableData: Map<String, String>) {
    val prefs = context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE)
    val isEnabled = prefs.getBoolean("pref_food_notification_enabled", true)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, FoodAlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        2001,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    if (!isEnabled) {
        alarmManager.cancel(pendingIntent)
        return
    }

    val foodDay = prefs.getString("pref_food_notification_day", "auto") ?: "auto"
    val foodHour = prefs.getInt("pref_food_notification_hour", 20)
    val foodMinute = prefs.getInt("pref_food_notification_minute", 0)

    val fullDaysList = listOf("شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه")
    val dayToCalendarDay = mapOf(
        "شنبه" to Calendar.SATURDAY,
        "یکشنبه" to Calendar.SUNDAY,
        "دوشنبه" to Calendar.MONDAY,
        "سه‌شنبه" to Calendar.TUESDAY,
        "چهارشنبه" to Calendar.WEDNESDAY,
        "پنجشنبه" to Calendar.THURSDAY,
        "جمعه" to Calendar.FRIDAY
    )

    val targetCalendarDay: Int = if (foodDay != "auto" && dayToCalendarDay.containsKey(foodDay)) {
        dayToCalendarDay[foodDay]!!
    } else {
        var firstClassDayIndex = 0 // default to Saturday
        for ((index, day) in fullDaysList.withIndex()) {
            val hasDayClass = tableData.entries.any { (k, v) ->
                k.startsWith("block_${day}_") && v.isNotBlank()
            }
            if (hasDayClass) {
                firstClassDayIndex = index
                break
            }
        }
        val targetDayIndex = (firstClassDayIndex - 4 + 7) % 7
        val targetDayName = fullDaysList[targetDayIndex]
        dayToCalendarDay[targetDayName]!!
    }

    val cal = Calendar.getInstance().apply {
        firstDayOfWeek = Calendar.SATURDAY
        set(Calendar.HOUR_OF_DAY, foodHour)
        set(Calendar.MINUTE, foodMinute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
    var daysUntilTarget = targetCalendarDay - currentDayOfWeek
    if (daysUntilTarget < 0) {
        daysUntilTarget += 7
    }
    cal.add(Calendar.DAY_OF_YEAR, daysUntilTarget)

    val now = System.currentTimeMillis()
    while (cal.timeInMillis <= now) {
        cal.add(Calendar.DAY_OF_YEAR, 7)
    }

    setAlarmAndAllowWhileIdle(alarmManager, cal.timeInMillis, pendingIntent)
}

fun scheduleAllAlarms(context: Context) {
    val prefs = context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE)
    val tableData = mutableMapOf<String, String>()
    prefs.all.forEach { (key, value) ->
        if ((key.startsWith("block_") || key.startsWith("duration_") || key.startsWith("room_")) && value is String) {
            tableData[key] = value
        }
    }
    scheduleFoodReservationAlarm(context, tableData)
    scheduleClassNotifications(context, tableData)
}

fun sendAdminBroadcastNotification(context: Context, title: String, message: String) {
    runCatching {
        val channelId = "admin_broadcast_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "اطلاعیه‌ها و اخبار همگانی",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "اعلان‌ها و اخبار ارسال شده توسط مدیریت"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            9999,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(9999, builder.build())
    }
}

fun sendClassNotification(context: Context, courseTitle: String, room: String) {
    val channelId = "class_reminder_channel"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "یادآوری کلاس‌ها",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "اطلاع‌رسانی‌های شروع و یادآوری کلاس‌های درسی"
        }
        notificationManager.createNotificationChannel(channel)
    }

    val notificationText = if (room.isNotBlank()) {
        "مهندس برو کلاس $room $courseTitle داری!"
    } else {
        "مهندس برو $courseTitle داری!"
    }

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        System.currentTimeMillis().toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notificationId = (System.currentTimeMillis() % 10000).toInt() + 3000

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(notificationText)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

    notificationManager.notify(notificationId, builder.build())
}

class ClassAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean("pref_class_notification_enabled", true)

        val courseTitle = intent.getStringExtra("EXTRA_COURSE_NAME") ?: ""
        val room = intent.getStringExtra("EXTRA_ROOM_NAME") ?: ""

        if (isEnabled && courseTitle.isNotBlank()) {
            sendClassNotification(context, courseTitle, room)
        }

        val tableData = mutableMapOf<String, String>()
        prefs.all.forEach { (key, value) ->
            if ((key.startsWith("block_") || key.startsWith("duration_") || key.startsWith("room_")) && value is String) {
                tableData[key] = value
            }
        }
        scheduleClassNotifications(context, tableData)
    }
}

private data class ClassScheduleItem(
    val dayStr: String,
    val slotKey: String,
    val courseTitle: String,
    val room: String,
    val startMinutes: Int,
    val endMinutes: Int
)

fun scheduleClassNotifications(context: Context, tableData: Map<String, String>) {
    val prefs = context.getSharedPreferences("MyUniPrefs", Context.MODE_PRIVATE)
    val isEnabled = prefs.getBoolean("pref_class_notification_enabled", true)
    val isWeekParityFlipped = prefs.getBoolean("isWeekParityFlipped", false)

    val fullDaysList = listOf("شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه")
    val dayToCalendarDay = mapOf(
        "شنبه" to Calendar.SATURDAY,
        "یکشنبه" to Calendar.SUNDAY,
        "دوشنبه" to Calendar.MONDAY,
        "سه‌شنبه" to Calendar.TUESDAY,
        "چهارشنبه" to Calendar.WEDNESDAY,
        "پنجشنبه" to Calendar.THURSDAY,
        "جمعه" to Calendar.FRIDAY
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val holdingSlotsList = listOf("8:30", "10:15", "12:30", "14:15", "16:00")

    if (!isEnabled) {
        fullDaysList.forEachIndexed { dayIndex, dayStr ->
            holdingSlotsList.forEach { slotKey ->
                val startMinutes = getSlotStartMinutes(slotKey)
                val requestCode = (dayIndex * 10000 + startMinutes).toInt()
                val intent = Intent(context, ClassAlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
            }
        }
        return
    }

    fullDaysList.forEach { dayStr ->
        val targetCalendarDay = dayToCalendarDay[dayStr] ?: return@forEach

        val nowCal = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.SATURDAY
        }
        val currentDayOfWeek = nowCal.get(Calendar.DAY_OF_WEEK)
        var daysUntilTarget = targetCalendarDay - currentDayOfWeek
        if (daysUntilTarget < 0) {
            daysUntilTarget += 7
        }

        val targetCal = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.SATURDAY
            timeInMillis = nowCal.timeInMillis
            add(Calendar.DAY_OF_YEAR, daysUntilTarget)
        }

        val isEvenWeek = isEvenWeekContinuous(targetCal.timeInMillis, isWeekParityFlipped)

        val holdingData = getHoldingTableData(tableData, isEvenWeek, fullDaysList)

        val dayClasses = mutableListOf<ClassScheduleItem>()

        holdingSlotsList.forEach { slotKey ->
            val blockKey = "block_${dayStr}_$slotKey"
            val courseTitle = holdingData[blockKey]
            if (!courseTitle.isNullOrBlank()) {
                val roomKey = "room_${dayStr}_$slotKey"
                val room = holdingData[roomKey] ?: ""
                val startMinutes = getSlotStartMinutes(slotKey)
                val endMinutes = getSlotEndMinutes(slotKey, "holding")

                dayClasses.add(
                    ClassScheduleItem(
                        dayStr = dayStr,
                        slotKey = slotKey,
                        courseTitle = courseTitle,
                        room = room,
                        startMinutes = startMinutes,
                        endMinutes = endMinutes
                    )
                )
            }
        }

        dayClasses.forEachIndexed { index, classItem ->
            val notifMinutes: Int = if (index == 0) {
                classItem.startMinutes - 30
            } else {
                when (classItem.slotKey) {
                    "10:15" -> 9 * 60 + 45   // 9:45
                    "12:30" -> 11 * 60 + 30  // 11:30
                    "14:15" -> 13 * 60 + 45  // 13:45
                    "16:00" -> 15 * 60 + 30  // 15:30
                    else -> classItem.startMinutes - 30
                }
            }

            if (notifMinutes >= 0) {
                val notifHour = notifMinutes / 60
                val notifMin = notifMinutes % 60

                val cal = Calendar.getInstance().apply {
                    firstDayOfWeek = Calendar.SATURDAY
                    timeInMillis = targetCal.timeInMillis
                    set(Calendar.HOUR_OF_DAY, notifHour)
                    set(Calendar.MINUTE, notifMin)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                if (cal.timeInMillis <= System.currentTimeMillis()) {
                    cal.add(Calendar.DAY_OF_YEAR, 7)
                }

                val dayIndex = fullDaysList.indexOf(dayStr).coerceAtLeast(0)
                val requestCode = (dayIndex * 10000 + classItem.startMinutes).toInt()

                val intent = Intent(context, ClassAlarmReceiver::class.java).apply {
                    putExtra("EXTRA_COURSE_NAME", classItem.courseTitle)
                    putExtra("EXTRA_ROOM_NAME", classItem.room)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                try {
                    setAlarmAndAllowWhileIdle(alarmManager, cal.timeInMillis, pendingIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

fun setAlarmAndAllowWhileIdle(alarmManager: AlarmManager, triggerAtMillis: Long, pendingIntent: PendingIntent) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    } catch (e: SecurityException) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun openBatteryOptimizationSettings(context: Context) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(context.packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
                context.startActivity(intent)
                return
            }
        }
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
        } catch (e2: Exception) {
            Toast.makeText(context, "امکان باز کردن تنظیمات نیست.", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun NotificationSettingsCard(
    isClassNotificationEnabled: Boolean,
    onClassNotificationChange: (Boolean) -> Unit,
    isFoodNotificationEnabled: Boolean,
    onFoodNotificationChange: (Boolean) -> Unit,
    foodNotificationDay: String,
    onFoodNotificationDayChange: (String) -> Unit,
    foodNotificationHour: Int,
    foodNotificationMinute: Int,
    onFoodNotificationTimeChange: (hour: Int, minute: Int) -> Unit,
    selectedColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val switchColors = if (isSystemInDarkTheme()) {
        SwitchDefaults.colors(
            checkedThumbColor = Color.White
        )
    } else {
        SwitchDefaults.colors()
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = selectedColor.copy(alpha = 0.06f),
        border = BorderStroke(1.5.dp, selectedColor.copy(alpha = 0.4f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("اعلان‌ها", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)

            // Class Notification Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClassNotificationChange(!isClassNotificationEnabled) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("یادآوری کلاس‌ها", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Switch(
                    checked = isClassNotificationEnabled,
                    onCheckedChange = { onClassNotificationChange(it) },
                    colors = switchColors
                )
            }

            // Food Notification Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFoodNotificationChange(!isFoodNotificationEnabled) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("یادآوری رزرو غذا", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Switch(
                    checked = isFoodNotificationEnabled,
                    onCheckedChange = { onFoodNotificationChange(it) },
                    colors = switchColors
                )
            }

            // Food Notification Day and Time Settings
            var showDayPickerDialog by remember { mutableStateOf(false) }

                val displayDayText = when (foodNotificationDay) {
                    "auto" -> "خودکار"
                    else -> foodNotificationDay
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDayPickerDialog = true }
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("روز ارسال یادآوری غذا", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = selectedColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, selectedColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = displayDayText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = selectedColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                if (showDayPickerDialog) {
                    CustomDayPickerDialog(
                        initialDayKey = foodNotificationDay,
                        selectedColor = selectedColor,
                        onDismiss = { showDayPickerDialog = false },
                        onConfirm = { key ->
                            onFoodNotificationDayChange(key)
                            showDayPickerDialog = false
                        }
                    )
                }

                var showTimePickerDialog by remember { mutableStateOf(false) }
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", foodNotificationHour, foodNotificationMinute)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePickerDialog = true }
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ساعت ارسال یادآوری غذا", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = selectedColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, selectedColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = formattedTime,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = selectedColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                if (showTimePickerDialog) {
                    CustomTimePickerDialog(
                        initialHour = foodNotificationHour,
                        initialMinute = foodNotificationMinute,
                        selectedColor = selectedColor,
                        onDismiss = { showTimePickerDialog = false },
                        onConfirm = { h, m ->
                            onFoodNotificationTimeChange(h, m)
                            showTimePickerDialog = false
                        }
                    )
                }


        }
    }
}

object JalaliCalendarHelper {
    val monthNames = listOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    )

    fun getCurrentJalaliYearMonthDay(): Triple<Int, Int, Int> {
        val cal = Calendar.getInstance()
        return gregorianToJalali(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getCurrentJalaliDateString(): String {
        val (y, m, d) = getCurrentJalaliYearMonthDay()
        return formatJalaliDate(y, m, d)
    }

    fun getJalaliDateOneWeekLaterString(): String {
        val cal = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 6)
        }
        val (y, m, d) = gregorianToJalali(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
        return formatJalaliDate(y, m, d)
    }

    fun jalaliToGregorian(jY: Int, jM: Int, jD: Int): Triple<Int, Int, Int> {
        val jy = jY - 979
        val jm = jM - 1
        val jd = jD - 1

        var jDayNo = 365 * jy + (jy / 33) * 8 + ((jy % 33) + 3) / 4
        for (i in 0 until jm) {
            jDayNo += if (i < 6) 31 else 30
        }
        jDayNo += jd

        var gDayNo = jDayNo + 79

        var gy = 1600 + 400 * (gDayNo / 146097)
        gDayNo %= 146097

        var leap = true
        if (gDayNo >= 36525) {
            gDayNo--
            gy += 100 * (gDayNo / 36524)
            gDayNo %= 36524

            if (gDayNo >= 365) {
                gDayNo++
            } else {
                leap = false
            }
        }

        gy += 4 * (gDayNo / 1461)
        gDayNo %= 1461

        if (gDayNo >= 366) {
            leap = false
            gDayNo--
            gy += gDayNo / 365
            gDayNo %= 365
        }

        val gDaysInMonth = intArrayOf(0, 31, if (leap) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gm = 0
        while (gm < 12 && gDayNo >= gDaysInMonth[gm + 1]) {
            gDayNo -= gDaysInMonth[gm + 1]
            gm++
        }
        gm++
        val gd = gDayNo + 1

        return Triple(gy, gm, gd)
    }

    fun gregorianToJalali(gY: Int, gM: Int, gD: Int): Triple<Int, Int, Int> {
        val gDaysInMonth = intArrayOf(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gy = gY - 1600
        var gm = gM - 1
        var gd = gD - 1

        var gDayNo = 365 * gy + (gy + 3) / 4 - (gy + 99) / 100 + (gy + 399) / 400
        for (i in 0 until gm) {
            gDayNo += gDaysInMonth[i + 1]
        }
        if (gm > 1 && ((gY % 4 == 0 && gY % 100 != 0) || (gY % 400 == 0))) {
            gDayNo++
        }
        gDayNo += gd

        var jDayNo = gDayNo - 79

        val jNp = jDayNo / 12053
        jDayNo %= 12053

        var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
        jDayNo %= 1461

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365
            jDayNo = (jDayNo - 1) % 365
        }

        var jm = 0
        var jd = 0
        for (i in 0 until 12) {
            val days = if (i < 6) 31 else if (i < 11) 30 else if (isJalaliLeapYear(jy)) 30 else 29
            if (jDayNo < days) {
                jm = i + 1
                jd = jDayNo + 1
                break
            }
            jDayNo -= days
        }
        return Triple(jy, jm, jd)
    }

    fun isJalaliLeapYear(year: Int): Boolean {
        val r = year % 33
        return r == 1 || r == 5 || r == 9 || r == 13 || r == 17 || r == 22 || r == 26 || r == 30
    }

    fun getDaysInJalaliMonth(year: Int, month: Int): Int {
        return when {
            month in 1..6 -> 31
            month in 7..11 -> 30
            month == 12 -> if (isJalaliLeapYear(year)) 30 else 29
            else -> 30
        }
    }

    fun parseJalaliDateString(dateStr: String): Triple<Int, Int, Int> {
        val clean = dateStr.trim().replace('-', '/')
        val parts = clean.split('/')
        if (parts.size == 3) {
            val y = parts[0].toIntOrNull()
            val m = parts[1].toIntOrNull()
            val d = parts[2].toIntOrNull()
            if (y != null && m != null && d != null && y in 1300..1500 && m in 1..12 && d in 1..31) {
                return Triple(y, m, d)
            }
        }
        return getCurrentJalaliYearMonthDay()
    }

    fun formatJalaliDate(year: Int, month: Int, day: Int): String {
        return String.format(Locale.US, "%04d/%02d/%02d", year, month, day)
    }
}

@Composable
fun WheelTextPicker(
    selectedIndex: Int,
    onIndexChange: (Int) -> Unit,
    items: List<String>,
    modifier: Modifier = Modifier,
    visibleCount: Int = 3,
    itemHeight: Dp = 44.dp,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    isLooping: Boolean = false
) {
    val count = items.size
    if (count <= 0) return

    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val totalItems = if (isLooping) count * 1000 else count
    val middleIndex = if (isLooping) (1000 / 2) * count else 0
    val targetIndexForIdx = { idx: Int ->
        val offset = idx.coerceIn(0, count - 1)
        middleIndex + offset
    }

    val lazyListState = remember(items.size, isLooping) {
        LazyListState(firstVisibleItemIndex = targetIndexForIdx(selectedIndex))
    }
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

    LaunchedEffect(selectedIndex, items, isLooping) {
        val currentOffset = lazyListState.firstVisibleItemScrollOffset
        val baseIndex = lazyListState.firstVisibleItemIndex
        val adjustedIndex = if (currentOffset > itemHeightPx / 2) baseIndex + 1 else baseIndex
        val currentIdx = if (isLooping) (adjustedIndex % count + count) % count else adjustedIndex.coerceIn(0, count - 1)
        if (currentIdx != selectedIndex) {
            lazyListState.scrollToItem(targetIndexForIdx(selectedIndex))
        }
    }

    val currentSelectedIndex by remember(count, isLooping) {
        derivedStateOf {
            val offset = lazyListState.firstVisibleItemScrollOffset
            val baseIndex = lazyListState.firstVisibleItemIndex
            val adjustedIndex = if (offset > itemHeightPx / 2) baseIndex + 1 else baseIndex
            if (isLooping) (adjustedIndex % count + count) % count else adjustedIndex.coerceIn(0, count - 1)
        }
    }

    LaunchedEffect(currentSelectedIndex) {
        if (currentSelectedIndex != selectedIndex && (lazyListState.isScrollInProgress || lazyListState.firstVisibleItemScrollOffset != 0)) {
            onIndexChange(currentSelectedIndex)
            try {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            } catch (_: Exception) {}
        }
    }

    val paddingVertical = itemHeight * (visibleCount / 2)

    LazyColumn(
        state = lazyListState,
        flingBehavior = snapFlingBehavior,
        contentPadding = PaddingValues(vertical = paddingVertical),
        modifier = modifier
            .height(itemHeight * visibleCount)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            count = totalItems,
            key = { index -> index }
        ) { index ->
            val itemText = if (isLooping) items[(index % count + count) % count] else items[index.coerceIn(0, count - 1)]

            val currentScrollPosition = lazyListState.firstVisibleItemIndex +
                    (if (itemHeightPx > 0) lazyListState.firstVisibleItemScrollOffset.toFloat() / itemHeightPx else 0f)
            val distanceFromCenter = kotlin.math.abs(index - currentScrollPosition)

            val isSelected = distanceFromCenter <= 0.5f

            val alpha = when {
                distanceFromCenter <= 0.5f -> 1.0f - (distanceFromCenter * 0.3f)
                distanceFromCenter <= 1.5f -> 0.7f - ((distanceFromCenter - 0.5f) * 0.35f)
                distanceFromCenter <= 2.5f -> 0.35f - ((distanceFromCenter - 1.5f) * 0.2f)
                else -> 0.15f
            }.coerceIn(0.12f, 1.0f)

            val scale = when {
                distanceFromCenter <= 0.5f -> 1.15f - (distanceFromCenter * 0.2f)
                distanceFromCenter <= 1.5f -> 0.95f - ((distanceFromCenter - 0.5f) * 0.2f)
                else -> 0.75f
            }.coerceIn(0.7f, 1.2f)

            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(index)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = itemText,
                    fontSize = if (isSelected) 17.sp else 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        }
                )
            }
        }
    }
}

@Composable
fun WheelDatePicker(
    year: Int,
    month: Int,
    day: Int,
    onDateChange: (year: Int, month: Int, day: Int) -> Unit,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    yearRange: IntRange = 1400..1420
) {
    var currentYear by remember(year) { mutableStateOf(year) }
    var currentMonth by remember(month) { mutableStateOf(month) }
    var currentDay by remember(day) { mutableStateOf(day) }

    val maxDays = JalaliCalendarHelper.getDaysInJalaliMonth(currentYear, currentMonth)

    LaunchedEffect(currentYear, currentMonth) {
        val daysInMonth = JalaliCalendarHelper.getDaysInJalaliMonth(currentYear, currentMonth)
        if (currentDay > daysInMonth) {
            currentDay = daysInMonth
        }
        onDateChange(currentYear, currentMonth, currentDay)
    }

    LaunchedEffect(currentDay) {
        onDateChange(currentYear, currentMonth, currentDay)
    }

    val itemHeight = 44.dp
    val visibleCount = 3

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Header labels for Year, Month, Day
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Row(
                modifier = Modifier.fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "سال",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = selectedColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "ماه",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = selectedColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1.2f)
                )
                Text(
                    text = "روز",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = selectedColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Wheel Box containing selection overlay & wheel pickers
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                    RoundedCornerShape(18.dp)
                )
                .padding(vertical = 10.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Selection Box overlay in exact vertical center
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(itemHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(selectedColor.copy(alpha = 0.12f))
                    .border(1.5.dp, selectedColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            )

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Year Wheel (Left)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        WheelNumberPicker(
                            value = currentYear,
                            onValueChange = { newYear ->
                                currentYear = newYear
                            },
                            range = yearRange,
                            visibleCount = visibleCount,
                            itemHeight = itemHeight,
                            selectedColor = selectedColor,
                            format = "%d"
                        )
                    }

                    Text(
                        text = "/",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = selectedColor.copy(alpha = 0.6f)
                    )

                    // Month Wheel (Center)
                    Box(
                        modifier = Modifier.weight(1.2f),
                        contentAlignment = Alignment.Center
                    ) {
                        WheelTextPicker(
                            selectedIndex = (currentMonth - 1).coerceIn(0, 11),
                            onIndexChange = { newMonthIdx ->
                                currentMonth = newMonthIdx + 1
                            },
                            items = JalaliCalendarHelper.monthNames,
                            visibleCount = visibleCount,
                            itemHeight = itemHeight,
                            selectedColor = selectedColor
                        )
                    }

                    Text(
                        text = "/",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = selectedColor.copy(alpha = 0.6f)
                    )

                    // Day Wheel (Right)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        WheelNumberPicker(
                            value = currentDay.coerceAtMost(maxDays),
                            onValueChange = { newDay ->
                                currentDay = newDay
                            },
                            range = 1..maxDays,
                            visibleCount = visibleCount,
                            itemHeight = itemHeight,
                            selectedColor = selectedColor,
                            format = "%02d"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDatePickerDialog(
    initialYear: Int,
    initialMonth: Int,
    initialDay: Int,
    selectedColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (year: Int, month: Int, day: Int) -> Unit
) {
    var selectedYear by remember { mutableStateOf(initialYear) }
    var selectedMonth by remember { mutableStateOf(initialMonth) }
    var selectedDay by remember { mutableStateOf(initialDay) }

    RtlAlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = selectedColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "تنظیم تاریخ ددلاین",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = selectedColor
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WheelDatePicker(
                    year = selectedYear,
                    month = selectedMonth,
                    day = selectedDay,
                    onDateChange = { y, m, d ->
                        selectedYear = y
                        selectedMonth = m
                        selectedDay = d
                    },
                    selectedColor = selectedColor
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedYear, selectedMonth, selectedDay)
                },
                colors = ButtonDefaults.buttonColors(containerColor = selectedColor),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("تایید", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("لغو")
            }
        }
    )
}

@Composable
fun WheelNumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier,
    visibleCount: Int = 5,
    itemHeight: Dp = 44.dp,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    format: String = "%02d",
    isLooping: Boolean = true
) {
    val count = range.count()
    if (count <= 0) return

    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val totalItems = if (isLooping) count * 1000 else count
    val middleIndex = if (isLooping) (1000 / 2) * count else 0
    val targetIndexForValue = { valVal: Int ->
        val offset = (valVal - range.first).coerceIn(0, count - 1)
        middleIndex + offset
    }

    // Keying LazyListState by range bounds ensures firstVisibleItemIndex is computed
    // synchronously on the exact frame range/maxDays changes, completely eliminating any 1-frame visual jump!
    val lazyListState = remember(range.first, range.last, isLooping) {
        LazyListState(firstVisibleItemIndex = targetIndexForValue(value))
    }
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

    // Sync lazyListState position if value changes externally without range changing
    LaunchedEffect(value, isLooping) {
        val currentOffset = lazyListState.firstVisibleItemScrollOffset
        val baseIndex = lazyListState.firstVisibleItemIndex
        val adjustedIndex = if (currentOffset > itemHeightPx / 2) baseIndex + 1 else baseIndex
        val rawValue = if (isLooping) (adjustedIndex % count + count) % count else adjustedIndex.coerceIn(0, count - 1)
        val currentVal = range.first + rawValue
        if (currentVal != value && !lazyListState.isScrollInProgress) {
            lazyListState.scrollToItem(targetIndexForValue(value))
        }
    }

    val selectedIndex by remember(range.first, range.last, count, isLooping) {
        derivedStateOf {
            val offset = lazyListState.firstVisibleItemScrollOffset
            val baseIndex = lazyListState.firstVisibleItemIndex
            val adjustedIndex = if (offset > itemHeightPx / 2) baseIndex + 1 else baseIndex
            val rawValue = if (isLooping) (adjustedIndex % count + count) % count else adjustedIndex.coerceIn(0, count - 1)
            range.first + rawValue
        }
    }

    LaunchedEffect(selectedIndex) {
        if (selectedIndex != value && (lazyListState.isScrollInProgress || lazyListState.firstVisibleItemScrollOffset != 0)) {
            onValueChange(selectedIndex)
            try {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            } catch (_: Exception) {}
        }
    }

    val paddingVertical = itemHeight * (visibleCount / 2)

    LazyColumn(
        state = lazyListState,
        flingBehavior = snapFlingBehavior,
        contentPadding = PaddingValues(vertical = paddingVertical),
        modifier = modifier
            .height(itemHeight * visibleCount)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            count = totalItems,
            key = { index -> index }
        ) { index ->
            val itemValue = if (isLooping) range.first + ((index % count + count) % count) else range.first + index.coerceIn(0, count - 1)

            val currentScrollPosition = lazyListState.firstVisibleItemIndex +
                    (if (itemHeightPx > 0) lazyListState.firstVisibleItemScrollOffset.toFloat() / itemHeightPx else 0f)
            val distanceFromCenter = kotlin.math.abs(index - currentScrollPosition)

            val isSelected = distanceFromCenter <= 0.5f

            val alpha = when {
                distanceFromCenter <= 0.5f -> 1.0f - (distanceFromCenter * 0.3f)
                distanceFromCenter <= 1.5f -> 0.7f - ((distanceFromCenter - 0.5f) * 0.35f)
                distanceFromCenter <= 2.5f -> 0.35f - ((distanceFromCenter - 1.5f) * 0.2f)
                else -> 0.15f
            }.coerceIn(0.12f, 1.0f)

            val scale = when {
                distanceFromCenter <= 0.5f -> 1.15f - (distanceFromCenter * 0.2f)
                distanceFromCenter <= 1.5f -> 0.95f - ((distanceFromCenter - 0.5f) * 0.2f)
                else -> 0.75f
            }.coerceIn(0.7f, 1.2f)

            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(index)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format(Locale.getDefault(), format, itemValue),
                    fontSize = if (isSelected) 22.sp else 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        }
                )
            }
        }
    }
}

@Composable
fun WheelTimePicker(
    hour: Int,
    minute: Int,
    onTimeChange: (hour: Int, minute: Int) -> Unit,
    selectedColor: Color,
    modifier: Modifier = Modifier
) {
    var currentHour by remember(hour) { mutableStateOf(hour) }
    var currentMinute by remember(minute) { mutableStateOf(minute) }

    val itemHeight = 44.dp
    val visibleCount = 5

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Wheel Box containing only the selection overlay & wheel pickers
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                    RoundedCornerShape(18.dp)
                )
                .padding(vertical = 10.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            // Selection Box overlay in exact vertical center
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(itemHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(selectedColor.copy(alpha = 0.12f))
                    .border(1.5.dp, selectedColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            )

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hour Wheel (Left)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        WheelNumberPicker(
                            value = currentHour,
                            onValueChange = { newHour ->
                                currentHour = newHour
                                onTimeChange(currentHour, currentMinute)
                            },
                            range = 0..23,
                            visibleCount = visibleCount,
                            itemHeight = itemHeight,
                            selectedColor = selectedColor
                        )
                    }

                    // Colon separator (Center)
                    Text(
                        text = ":",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = selectedColor,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // Minute Wheel (Right)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        WheelNumberPicker(
                            value = currentMinute,
                            onValueChange = { newMinute ->
                                currentMinute = newMinute
                                onTimeChange(currentHour, currentMinute)
                            },
                            range = 0..59,
                            visibleCount = visibleCount,
                            itemHeight = itemHeight,
                            selectedColor = selectedColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    selectedColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var selectedHour by remember { mutableStateOf(initialHour) }
    var selectedMinute by remember { mutableStateOf(initialMinute) }

    RtlAlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "تنظیم ساعت یادآوری",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = selectedColor
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WheelTimePicker(
                    hour = selectedHour,
                    minute = selectedMinute,
                    onTimeChange = { h, m ->
                        selectedHour = h
                        selectedMinute = m
                    },
                    selectedColor = selectedColor
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedHour, selectedMinute)
                },
                colors = ButtonDefaults.buttonColors(containerColor = selectedColor),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("تایید", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("لغو")
            }
        }
    )
}

@Composable
fun CustomDayPickerDialog(
    initialDayKey: String,
    selectedColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val dayOptions = listOf(
        "auto" to "خودکار",
        "شنبه" to "شنبه",
        "یکشنبه" to "یکشنبه",
        "دوشنبه" to "دوشنبه",
        "سه‌شنبه" to "سه‌شنبه",
        "چهارشنبه" to "چهارشنبه",
        "پنجشنبه" to "پنجشنبه",
        "جمعه" to "جمعه"
    )

    RtlAlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "تنظیم روز ارسال یادآوری غذا",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = selectedColor
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dayOptions.forEach { (key, label) ->
                    val isSelected = initialDayKey == key

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) selectedColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                onConfirm(key)
                                onDismiss()
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) selectedColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurface
                            )

                            androidx.compose.material3.RadioButton(
                                selected = isSelected,
                                onClick = null,
                                colors = androidx.compose.material3.RadioButtonDefaults.colors(
                                    selectedColor = selectedColor,
                                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CustomDarkModePickerDialog(
    initialMode: String,
    selectedColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val options = listOf(
        "light" to "لایت",
        "dark" to "دارک",
        "system" to "طبق سیستم"
    )

    RtlAlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "حالت تم",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = selectedColor
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { (modeKey, modeLabel) ->
                    val isSelected = initialMode == modeKey

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) selectedColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                onConfirm(modeKey)
                                onDismiss()
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) selectedColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = modeLabel,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurface
                            )

                            androidx.compose.material3.RadioButton(
                                selected = isSelected,
                                onClick = null,
                                colors = androidx.compose.material3.RadioButtonDefaults.colors(
                                    selectedColor = selectedColor,
                                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }
    )
}

fun openExactAlarmSettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        try {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            openBatteryOptimizationSettings(context)
        }
    } else {
        openBatteryOptimizationSettings(context)
    }
}

@Composable
fun PdfExtractionGuideDialog(
    onDismiss: () -> Unit,
    onSelectPdf: () -> Unit,
    selectedColor: Color
) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(0) }
    val totalSteps = 10

    var scale by remember(currentStep) { mutableStateOf(AppConfig.golestanGuideImageScale) }
    var offsetX by remember(currentStep) { mutableStateOf(AppConfig.golestanGuideInitialOffsetX) }
    var offsetY by remember(currentStep) { mutableStateOf(AppConfig.golestanGuideInitialOffsetY) }

    val stepsDescriptions = listOf(
        "1: ابتدا وارد سایت گلستان شوید.",
        "2: گزارش ۷۳ را وارد و دکمه تایید را بزنید.",
        "3: گزینه \"مشاهده گزارش\" را بزنید.",
        "4: گزینه \"آماده‌سازی جهت چاپ\" را بزنید.",
        "5: گزینه \"Ctrl+P چاپ\" را بزنید.",
        "6: منو تنظیمات PDF را باز کنید.",
        "7: قسمت ابعاد \"Paper size\" را باز کنید.",
        "8: گزینه \"Arch B\" را بزنید.",
        "9: روی گزینه زردرنگ جهت دانلود PDF بزنید.",
        "10: فایل PDF را ذخیره کنید."
    )

    val imageBitmap = remember(currentStep) {
        try {
            val fileName = "${currentStep + 1}.jpg"
            context.assets.open(fileName).use { inputStream ->
                android.graphics.BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: Exception) {
            null
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Surface(
                shape = RoundedCornerShape(AppConfig.dialogCornerRadius),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = AppConfig.dialogTonalElevation,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .padding(AppConfig.dialogPadding)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppConfig.dialogContentSpacing)
                ) {
                    // Title Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "راهنمای استخراج برنامه از گلستان",
                            fontSize = AppConfig.dialogTitleFontSize,
                            fontWeight = AppConfig.dialogTitleFontWeight,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${currentStep + 1} / $totalSteps",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = selectedColor
                        )
                    }

                    // Image container with zoom and pan support
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .then(
                                if (AppConfig.golestanGuideBoxWidth > 0.dp && AppConfig.golestanGuideBoxWidth != Dp.Unspecified) {
                                    Modifier.width(AppConfig.golestanGuideBoxWidth)
                                } else {
                                    Modifier.fillMaxWidth()
                                }
                            )
                            .widthIn(min = AppConfig.golestanGuideBoxMinWidth, max = AppConfig.golestanGuideBoxMaxWidth)
                            .heightIn(min = AppConfig.golestanGuideBoxMinHeight, max = AppConfig.golestanGuideBoxMaxHeight)
                            .clip(RoundedCornerShape(AppConfig.golestanGuideBoxCornerRadius))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = AppConfig.golestanGuideBoxBgAlpha))
                            .border(
                                AppConfig.golestanGuideBoxBorderWidth,
                                selectedColor.copy(alpha = AppConfig.golestanGuideBoxBorderAlpha),
                                RoundedCornerShape(AppConfig.golestanGuideBoxCornerRadius)
                            )
                            .pointerInput(currentStep) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(AppConfig.golestanGuideMinScale, AppConfig.golestanGuideMaxScale)
                                    if (scale > AppConfig.golestanGuideMinScale) {
                                        offsetX += pan.x
                                        offsetY += pan.y
                                    } else {
                                        offsetX = AppConfig.golestanGuideInitialOffsetX
                                        offsetY = AppConfig.golestanGuideInitialOffsetY
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = "Step ${currentStep + 1}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer(
                                        scaleX = scale,
                                        scaleY = scale,
                                        translationX = offsetX,
                                        translationY = offsetY
                                    ),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Text(
                                text = "تصویر مرحله ${currentStep + 1} یافت نشد",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(32.dp)
                            )
                        }
                    }

                    // Description Surface
                    Surface(
                        shape = RoundedCornerShape(AppConfig.dialogInnerCornerRadius),
                        color = selectedColor.copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stepsDescriptions[currentStep],
                            fontSize = AppConfig.dialogBodyFontSize,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Start
                        )
                    }

                    // Buttons Row: Next (Right) & Previous (Left)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentStep < totalSteps - 1) {
                            Button(
                                onClick = { if (currentStep < totalSteps - 1) currentStep++ },
                                shape = RoundedCornerShape(AppConfig.dialogButtonCornerRadius),
                                colors = ButtonDefaults.buttonColors(containerColor = selectedColor),
                                modifier = Modifier.width(120.dp).height(44.dp)
                            ) {
                                Text("بعدی", fontWeight = FontWeight.Bold, fontSize = AppConfig.dialogButtonFontSize)
                            }
                        } else {
                            Button(
                                onClick = {
                                    onDismiss()
                                    onSelectPdf()
                                },
                                shape = RoundedCornerShape(AppConfig.dialogButtonCornerRadius),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                modifier = Modifier.height(44.dp)
                            ) {
                                Text("انتخاب فایل PDF", fontWeight = FontWeight.Bold, fontSize = AppConfig.dialogButtonFontSize, color = Color.White)
                            }
                        }

                        Button(
                            onClick = { if (currentStep > 0) currentStep-- },
                            enabled = currentStep > 0,
                            shape = RoundedCornerShape(AppConfig.dialogButtonCornerRadius),
                            colors = ButtonDefaults.buttonColors(containerColor = selectedColor),
                            modifier = Modifier.width(120.dp).height(44.dp)
                        ) {
                            Text("قبلی", fontWeight = FontWeight.Bold, fontSize = AppConfig.dialogButtonFontSize)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RtlAlertDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null
) {
    Dialog(onDismissRequest = onDismissRequest) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Surface(
                shape = RoundedCornerShape(AppConfig.dialogCornerRadius),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = AppConfig.dialogTonalElevation,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(AppConfig.dialogPadding),
                    verticalArrangement = Arrangement.spacedBy(AppConfig.dialogContentSpacing)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        title()
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        text()
                    }
                    if (confirmButton != null || dismissButton != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (dismissButton != null) {
                                dismissButton()
                                if (confirmButton != null) {
                                    Spacer(modifier = Modifier.width(AppConfig.dialogButtonSpacing))
                                }
                            }
                            if (confirmButton != null) {
                                confirmButton()
                            }
                        }
                    }
                }
            }
        }
    }
}
