package com.example.myuni

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ParsedCoursesDialog(
    initialCourses: List<ParsedCourse>,
    themeColor: Color,
    onApplyCourses: (List<ParsedCourse>) -> Unit,
    onDismiss: () -> Unit,
) {
    val courses = remember {
        mutableStateListOf<ParsedCourse>().apply {
            addAll(initialCourses)
        }
    }

    val selectedCount = courses.count { it.isSelected }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(AppConfig.dialogCornerRadius),
        tonalElevation = AppConfig.dialogTonalElevation,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(AppConfig.dialogButtonSpacing))
                Text(
                    text = AppConfig.DialogTexts.parsedCoursesTitle,
                    fontWeight = AppConfig.dialogTitleFontWeight,
                    fontSize = AppConfig.dialogTitleFontSize,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (courses.isEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(AppConfig.dialogInnerCornerRadius),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = AppConfig.DialogTexts.parsedCoursesEmptyMessage,
                            fontSize = AppConfig.dialogBodyFontSize,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                } else {
                    Text(
                        text = "تعداد ${courses.size} کلاس درس شناسایی شد. می‌توانید آن‌ها را بررسی، ویرایش یا انتخاب کنید:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            courses.add(
                                ParsedCourse(
                                    title = "درس جدید",
                                    day = "شنبه",
                                    startTime = "08:00",
                                    endTime = "10:00",
                                    slotKey = "8",
                                    duration = 2,
                                    isSelected = true
                                )
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("افزودن درس دستی", fontSize = 12.sp)
                    }

                    if (courses.isNotEmpty()) {
                        Row {
                            TextButton(
                                onClick = { courses.forEach { it.isSelected = true } },
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                            ) {
                                Text("انتخاب همه", fontSize = 11.sp, color = themeColor)
                            }
                            TextButton(
                                onClick = { courses.forEach { it.isSelected = false } },
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                            ) {
                                Text("لغو همه", fontSize = 11.sp, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }

                // List of courses
                if (courses.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp, max = 320.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(courses, key = { it.id }) { course ->
                            ParsedCourseCard(
                                course = course,
                                themeColor = themeColor,
                                onDelete = { courses.remove(course) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val selected = courses.filter { it.isSelected && it.title.isNotBlank() }
                    onApplyCourses(selected)
                },
                enabled = selectedCount > 0,
                colors = ButtonDefaults.buttonColors(containerColor = themeColor)
            ) {
                Text("افزودن $selectedCount درس به جدول", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("لغو")
            }
        }
    )
}

@Composable
fun ParsedCourseCard(
    course: ParsedCourse,
    themeColor: Color,
    onDelete: () -> Unit,
) {
    var titleText by remember { mutableStateOf(course.title) }
    var roomText by remember { mutableStateOf(course.room) }
    var isChecked by remember { mutableStateOf(course.isSelected) }

    Surface(
        color = if (isChecked) themeColor.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isChecked) themeColor.copy(alpha = 0.3f) else Color.Transparent),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    course.isSelected = it
                },
                colors = CheckboxDefaults.colors(checkedColor = themeColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                // Title TextField
                BasicTextField(
                    value = titleText,
                    onValueChange = {
                        titleText = it
                        course.title = it
                    },
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (titleText.isEmpty()) {
                            Text("نام درس...", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Day & Time badge
                    Surface(
                        color = themeColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${course.day} (${course.startTime} - ${course.endTime})",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = themeColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Room TextField
                    BasicTextField(
                        value = roomText,
                        onValueChange = {
                            roomText = it
                            course.room = it
                        },
                        textStyle = TextStyle(
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (roomText.isEmpty()) {
                                Text("شماره کلاس...", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                            }
                            innerTextField()
                        }
                    )
                }

                if (course.examDate.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "امتحان پایان‌ترم: ${course.examDate}${if (course.examTime.isNotBlank()) " (${course.examTime})" else ""}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
