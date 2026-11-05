package com.example.myuni

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class CourseScheduleParserTest {

    @Test
    fun testRoomExtractionVariousFormats() {
        val sample1 = "ریاضی 1 شنبه 08:00 تا 10:00 کلاس{101}"
        val courses1 = CourseScheduleParser.parse(sample1)
        assertEquals(1, courses1.size)
        assertEquals("101", courses1[0].room)
        assertEquals("ریاضی 1", courses1[0].title)

        val sample2 = "فیزیک 2 دوشنبه 10:00 تا 12:00 کلاس{302}"
        val courses2 = CourseScheduleParser.parse(sample2)
        assertEquals(1, courses2.size)
        assertEquals("302", courses2[0].room)
        assertEquals("فیزیک 2", courses2[0].title)

        val sample3 = "مدار منطقی چهارشنبه 14:00 تا 16:00 کلاس{۱۰۵}"
        val courses3 = CourseScheduleParser.parse(sample3)
        assertEquals(1, courses3.size)
        assertEquals("105", courses3[0].room)

        val sample4 = "برنامه‌نویسی سه‌شنبه 08:30 تا 10:15 {204}کلاس"
        val courses4 = CourseScheduleParser.parse(sample4)
        assertEquals(1, courses4.size)
        assertEquals("204", courses4[0].room)

        val sample5 = "زبان عمومی پنجشنبه 08:00 تا 10:00 مکان: 405"
        val courses5 = CourseScheduleParser.parse(sample5)
        assertEquals(1, courses5.size)
        assertEquals("405", courses5[0].room)

        val sample6 = "آز فیزیک 2 شنبه 10:00 تا 12:00 کلاس{202}"
        val courses6 = CourseScheduleParser.parse(sample6)
        assertEquals(1, courses6.size)
        assertEquals("202", courses6[0].room)
        assertEquals("آز فیزیک 2", courses6[0].title)

        val sample7 = "آزمایشگاه فیزیک 2 دوشنبه 14:00 تا 16:00 آزمایشگاه 6"
        val courses7 = CourseScheduleParser.parse(sample7)
        assertEquals(1, courses7.size)
        assertEquals("آزمایشگاه 6", courses7[0].room)
        assertEquals("آزمایشگاه فیزیک 2", courses7[0].title)
    }

    @Test
    fun testExamDateAndExamTimeExtraction() {
        assertEquals("1403/10/25", CourseScheduleParser.extractExamDate("1403/10/25"))
        assertEquals("1403/10/25", CourseScheduleParser.extractExamDate("25/10/1403"))
        assertEquals("1403/01/05", CourseScheduleParser.extractExamDate("1403/1/5"))
        assertEquals("1403/10/25", CourseScheduleParser.extractExamDate("1403 / 10 / 25"))
        assertEquals("1403/10/25", CourseScheduleParser.extractExamDate("25 / 10 / 1403"))
        assertEquals("1403/10/25", CourseScheduleParser.extractExamDate("1403-10-25"))
        assertEquals("1403/10/25", CourseScheduleParser.extractExamDate("1403.10.25"))
        assertEquals("1403/10/25", CourseScheduleParser.extractExamDate("تاریخ امتحان: 1403/10/25 ساعت 08:00 تا 10:00"))
        assertEquals("1403/10/25", CourseScheduleParser.extractExamDate("3041/01/52"))

        assertEquals("08:00", CourseScheduleParser.extractExamTime("08:00 تا 10:00"))
        assertEquals("08:00", CourseScheduleParser.extractExamTime("10:00 - 08:00"))
        assertEquals("13:30", CourseScheduleParser.extractExamTime("13:30 تا 15:30"))
        assertEquals("10:00", CourseScheduleParser.extractExamTime("10:00"))
        assertEquals("08:30", CourseScheduleParser.extractExamTime("ساعت 08:30"))
    }

    @Test
    fun testParseActualPdfFile() {
        val pdfFile1 = File("""C:\Users\Shayan Endless\Desktop\کليه صفحات ( 1صفحه ) آماده گرديد(1).PDF""")
        val pdfFile2 = File("D:/MyUni/app/src/main/assets/template.PDF")
        val targetFile = if (pdfFile1.exists()) pdfFile1 else if (pdfFile2.exists()) pdfFile2 else null
        
        if (targetFile == null || !targetFile.exists()) {
            println("No test PDF file found.")
            return
        }

        val items = mutableListOf<PdfTextItem>()

        PDDocument.load(targetFile).use { document ->
            val stripper = object : PDFTextStripper() {
                init {
                    sortByPosition = true
                }

                override fun writeString(text: String, textPositions: MutableList<TextPosition>) {
                    val firstPos = textPositions.firstOrNull()
                    if (firstPos != null) {
                        items.add(PdfTextItem(text = text, x = firstPos.xDirAdj, y = firstPos.yDirAdj))
                    }
                }
            }
            val text = stripper.getText(document)
            
            val results = CourseScheduleParser.parse(text, items)
            println("=== ALL LEFT ITEMS (x < 150) ===")
            items.filter { it.x < 150f }.sortedBy { it.y }.forEach { item ->
                println("x=%.1f, y=%.1f : '%s'".format(item.x, item.y, item.text))
            }

            println("=== PARSED RESULTS (${results.size}) FROM ${targetFile.name} ===")
            results.forEachIndexed { index, course ->
                println("${index + 1}. Title: '${course.title}' | Day: '${course.day}' | Time: '${course.startTime}-${course.endTime}' | ExamDate: '${course.examDate}' | ExamTime: '${course.examTime}'")
            }
            
            org.junit.Assert.assertTrue("Should parse courses", results.isNotEmpty())
            org.junit.Assert.assertTrue("Should parse exam dates", results.any { it.examDate.isNotBlank() })
            // Assert that every course with an exam date also has an exam time extracted!
            val coursesWithDate = results.filter { it.examDate.isNotBlank() }
            val missingTime = coursesWithDate.filter { it.examTime.isBlank() }
            if (missingTime.isNotEmpty()) {
                val missingTitles = missingTime.joinToString { "'${it.title}' (date: ${it.examDate})" }
                org.junit.Assert.fail("Missing exam time for: $missingTitles")
            }
        }
    }
}
