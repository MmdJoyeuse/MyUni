package com.example.myuni

import java.util.Locale
import java.util.UUID

data class ParsedCourse(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var day: String,               // e.g. "شنبه"
    var startTime: String,         // e.g. "08:00"
    var endTime: String,           // e.g. "10:00"
    var slotKey: String,           // e.g. "8" or "8:30"
    var duration: Int = 1,         // e.g. 1 or 2
    var room: String = "",         // e.g. "102"
    var mode: String = "golestan", // "golestan" or "holding"
    var examDate: String = "",     // e.g. "1405/10/25"
    var examTime: String = "",     // e.g. "15:00 - 16:00"
    var isSelected: Boolean = true
)

object CourseScheduleParser {

    private val PERSIAN_DAYS = listOf("پنجشنبه", "چهارشنبه", "سه‌شنبه", "دوشنبه", "یکشنبه", "شنبه")
    private val TIME_REGEX = Regex("""(\d{1,2})[：:.]?(\d{2})?\s*(?:تا|-|–|—|الی|الى|_|/|~|to)\s*(\d{1,2})[：:.]?(\d{2})?\s*([0-9۰-۹٠-٩]{2,4})?""")

    private val FORWARD_ROOM_REGEX = Regex(
        """(?i)(?:^|[\s\-_:({\[\u200C])(کلاس|كلاس|مکان\s*برگزاری|مکان\s*تشکیل|مکان\s*کلاس|مکان|تالار|اتاق|دانشکده|سایت|آزمایشگاه|آزمايشگاه|آز|سالن|کارگاه|آمفی|ک\.|ک)\s*(?:شماره|شمارة|ش)?\s*[:：\-_#(){}\[\]«»"'\s]*([0-9۰-۹٠-٩a-zA-Z]+)[\}\]\)]*"""
    )
    private val REVERSE_ROOM_REGEX = Regex(
        """(?i)[{\[(]?\s*([0-9۰-۹٠-٩a-zA-Z]{2,5})\s*[})]?\s*[:：\-_#(){}\[\]«»"'\s]*(کلاس|كلاس|تالار|اتاق|دانشکده|سایت|آزمایشگاه|آزمايشگاه|آز|سالن|کارگاه|آمفی)(?:$|[\s\-_:)}\]\u200C])"""
    )
    private val BRACED_ROOM_REGEX = Regex("""[\{\[\(]\s*([0-9۰-۹٠-٩]{2,4})\s*[\}\]\)]""")
    private val STANDALONE_ROOM_REGEX = Regex("""(?:^|[^\d۰-۹])([0-9۰-۹]{3})(?:[^\d۰-۹]|$)""")

    private val IGNORED_ROOM_NUMBERS = setOf(
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "30", "45",
        "1400", "1401", "1402", "1403", "1404", "1405", "1406",
        "2023", "2024", "2025", "2026", "2027", "2028"
    )

    fun parse(extractedText: String, textItems: List<PdfTextItem> = emptyList()): List<ParsedCourse> {
        // Strategy 1: Grid Table Parsing using X-Y coordinates (Golestan Report 88 / 78)
        if (textItems.isNotEmpty()) {
            val gridCourses = parseGridTable(textItems)
            if (gridCourses.isNotEmpty()) {
                return deduplicateCourses(gridCourses)
            }
        }

        // Strategy 2: Text-based line & sliding window parsing
        val normalized = normalizeDigitsAndSpacing(extractedText)
        val courses = mutableListOf<ParsedCourse>()

        val lines = normalized.lines().map { it.trim() }.filter { it.isNotBlank() }

        val nonHeaderLines = lines.filterNot { isDayHeaderLine(it) }

        for (line in nonHeaderLines) {
            courses.addAll(parseChunk(line))
        }

        if (courses.size < 2 && nonHeaderLines.size > 1) {
            for (i in 0 until nonHeaderLines.size - 1) {
                val chunk2 = "${nonHeaderLines[i]} ${nonHeaderLines[i + 1]}"
                courses.addAll(parseChunk(chunk2))

                if (i < nonHeaderLines.size - 2) {
                    val chunk3 = "${nonHeaderLines[i]} ${nonHeaderLines[i + 1]} ${nonHeaderLines[i + 2]}"
                    courses.addAll(parseChunk(chunk3))
                }
            }
        }

        return deduplicateCourses(courses)
    }

    fun normalizeDayName(text: String): String? {
        val norm = normalizeDigitsAndSpacing(text).trim()
        if (!norm.contains("شنبه") && !norm.contains("جمعه")) return null

        return when {
            norm.contains("پنج") || norm.contains("۵") || norm.contains("5") -> "پنجشنبه"
            norm.contains("چهار") || norm.contains("۴") || norm.contains("4") -> "چهارشنبه"
            norm.contains("سه") || norm.contains("۳") || norm.contains("3") -> "سه‌شنبه"
            norm.contains("دو") || norm.contains("۲") || norm.contains("2") -> "دوشنبه"
            norm.contains("یک") || norm.contains("يك") || norm.contains("۱") || norm.contains("1") -> "یکشنبه"
            norm.contains("جمعه") -> "جمعه"
            norm.contains("شنبه") -> "شنبه"
            else -> null
        }
    }

    private fun parseGridTable(items: List<PdfTextItem>): List<ParsedCourse> {
        val result = mutableListOf<ParsedCourse>()

        // 1. Detect Header Day Columns dynamically
        val headerMap = mutableMapOf<String, Float>()
        var yHeader = 0f

        for (item in items) {
            if (item.y > 300f) continue
            val stdDay = normalizeDayName(item.text)
            if (stdDay != null) {
                if (!headerMap.containsKey(stdDay)) {
                    headerMap[stdDay] = item.x
                }
                if (item.y > yHeader) {
                    yHeader = item.y
                }
            }
        }

        if (headerMap.size < 2) return emptyList()

        val sortedHeaders = headerMap.entries.sortedBy { it.value }
        val dayBoundaries = mutableListOf<Triple<String, Float, Float>>()

        for (i in sortedHeaders.indices) {
            val current = sortedHeaders[i]
            val minX = if (i == 0) (current.value - 35f).coerceAtLeast(0f) else (sortedHeaders[i - 1].value + current.value) / 2f
            val maxX = if (i == sortedHeaders.size - 1) current.value + 35f else (current.value + sortedHeaders[i + 1].value) / 2f
            dayBoundaries.add(Triple(current.key, minX, maxX))
        }

        val allMinDayX = dayBoundaries.minOf { it.second }
        val allMaxDayX = dayBoundaries.maxOf { it.third }

        // 2. Identify Title Column location
        var titleMinX = 0f
        var titleMaxX = 0f

        val titleHeaderItem = items.firstOrNull { item ->
            item.y <= yHeader + 10f && (item.text.contains("نام درس") || item.text.contains("عنوان"))
        }

        if (titleHeaderItem != null) {
            titleMinX = titleHeaderItem.x - 45f
            titleMaxX = titleHeaderItem.x + 55f
        } else {
            // Find items outside day columns that look like course titles
            val titleCandidates = items.filter { item ->
                item.y > yHeader + 10f && (item.x < allMinDayX - 10f || item.x > allMaxDayX + 10f)
            }
            if (titleCandidates.isNotEmpty()) {
                val avgX = titleCandidates.map { it.x }.average().toFloat()
                titleMinX = avgX - 60f
                titleMaxX = avgX + 60f
            }
        }

        // 3. Find row Y starting positions (Y > yHeader + 5f)
        val contentItems = items.filter { it.y > yHeader + 5f }.sortedBy { it.y }
        if (contentItems.isEmpty()) return emptyList()

        // Gather candidate row Y positions: items with course codes, times, row numbers, or titles
        val candidateYs = mutableListOf<Float>()
        val courseCodeRegex = Regex("""\b\d{6,8}_\d{2}\b|\b\d{2}_\d{6,8}\b""")

        for (item in contentItems) {
            val norm = normalizeDigitsAndSpacing(item.text)
            val isTime = item.x >= allMinDayX - 10f && TIME_REGEX.containsMatchIn(norm)
            val isCode = courseCodeRegex.containsMatchIn(norm)
            val isTitle = item.x in titleMinX..titleMaxX && cleanCourseTitle(item.text).isNotBlank()
            val isRowIndex = item.x > allMaxDayX + 100f && norm.trim().all { it.isDigit() }
            val isExamInfo = item.x < allMinDayX - 10f && (extractExamDate(item.text) != null || extractExamTime(item.text) != null)

            if (isTime || isCode || isTitle || isRowIndex || isExamInfo) {
                candidateYs.add(item.y)
            }
        }

        if (candidateYs.isEmpty()) return emptyList()

        // Cluster candidate Ys that are within 12f of each other into distinct row Y levels
        val rowYs = mutableListOf<Float>()
        candidateYs.sort()
        for (y in candidateYs) {
            val last = rowYs.lastOrNull()
            if (last == null || (y - last) >= 12f) {
                rowYs.add(y)
            }
        }

        // 4. Pre-extract exam info for each course title block based on Y ranges
        data class CourseBlockInfo(
            val title: String,
            val startY: Float,
            val endY: Float,
            var examDate: String = "",
            var examTime: String = ""
        )

        val courseBlocks = mutableListOf<CourseBlockInfo>()
        var currentBlockTitle = ""

        for (i in rowYs.indices) {
            val y = rowYs[i]
            val rowTitleItems = items.filter { it.y in (y - 8f)..(y + 12f) && it.x in titleMinX..titleMaxX }
            val rawTitle = rowTitleItems.joinToString(" ") { it.text }
            val cleanTitle = cleanCourseTitle(rawTitle)

            if (cleanTitle.isNotBlank() && cleanTitle != currentBlockTitle) {
                if (currentBlockTitle.isNotBlank() && courseBlocks.isNotEmpty()) {
                    val lastIdx = courseBlocks.size - 1
                    courseBlocks[lastIdx] = courseBlocks[lastIdx].copy(endY = y)
                }
                currentBlockTitle = cleanTitle
                courseBlocks.add(CourseBlockInfo(title = cleanTitle, startY = y, endY = y + 50f))
            }
        }

        if (courseBlocks.isNotEmpty()) {
            val lastIdx = courseBlocks.size - 1
            courseBlocks[lastIdx] = courseBlocks[lastIdx].copy(endY = courseBlocks[lastIdx].startY + 60f)
        }

        for (block in courseBlocks) {
            val blockLeftItems = items.filter { item ->
                item.x < allMinDayX - 10f &&
                item.y >= (block.startY - 12f) &&
                item.y < block.endY
            }.sortedBy { it.y }

            val combinedLeft = blockLeftItems.joinToString(" ") { it.text }

            val d = extractExamDate(combinedLeft) ?: blockLeftItems.firstNotNullOfOrNull { extractExamDate(it.text) } ?: ""
            block.examDate = d

            val textWithoutDate = combinedLeft
                .replace(d, "")
                .replace(d.replace('/', '-'), "")
                .replace(d.replace('/', '.'), "")
                .replace(Regex("""\b\d{4}[./-]\d{1,2}[./-]\d{1,2}\b"""), "")
                .replace(Regex("""\b\d{1,2}[./-]\d{1,2}[./-]\d{4}\b"""), "")

            val t = extractExamTime(textWithoutDate) ?: blockLeftItems.firstNotNullOfOrNull { item ->
                val itemWithoutDate = item.text
                    .replace(d, "")
                    .replace(Regex("""\b\d{4}[./-]\d{1,2}[./-]\d{1,2}\b"""), "")
                extractExamTime(itemWithoutDate)
            } ?: ""
            block.examTime = t
        }

        val examInfoMap = courseBlocks.associateBy({ it.title }, { Pair(it.examDate, it.examTime) })

        // 5. Extract courses row by row
        var lastTitle = ""

        for (i in rowYs.indices) {
            val currentY = rowYs[i]
            val nextY = if (i < rowYs.size - 1) rowYs[i + 1] else currentY + 60f
            val minY = currentY - 8f
            val maxY = nextY - 8f

            val rowItems = items.filter { it.y in minY..maxY }

            // Title in this row
            val rowTitleItems = rowItems.filter { it.x in titleMinX..titleMaxX }
            val combinedRawTitle = rowTitleItems.joinToString(" ") { it.text }
            val cleanTitle = cleanCourseTitle(combinedRawTitle)

            if (cleanTitle.isNotBlank()) {
                lastTitle = cleanTitle
            }

            val currentTitle = if (cleanTitle.isNotBlank()) cleanTitle else lastTitle
            if (currentTitle.isBlank()) continue

            val (currentExamDate, currentExamTime) = examInfoMap[currentTitle] ?: Pair("", "")

            // Check each day column cell in this row
            for ((dayName, dayMinX, dayMaxX) in dayBoundaries) {
                val dayCellItems = rowItems.filter { it.x in (dayMinX - 5f)..(dayMaxX + 5f) }
                if (dayCellItems.isEmpty()) continue

                val cellText = dayCellItems.joinToString(" ") { it.text }
                val normCellText = normalizeDigitsAndSpacing(cellText)
                val timeMatches = TIME_REGEX.findAll(normCellText).toList()

                if (timeMatches.isEmpty()) continue

                var roomText = extractRoom(cellText)
                if (roomText.isBlank()) {
                    // Check items in adjacent vertical space in the same day column
                    val expandedNearby = items.filter { item ->
                        item.x in (dayMinX - 10f)..(dayMaxX + 10f) &&
                        item.y >= (currentY - 5f) && item.y <= (nextY + 15f)
                    }
                    roomText = extractRoom(expandedNearby.joinToString(" ") { it.text })
                }

                for (match in timeMatches) {
                    val rawH1 = match.groupValues[1].toIntOrNull() ?: continue
                    val rawM1 = match.groupValues[2].ifEmpty { "0" }.toIntOrNull() ?: 0
                    val rawH2 = match.groupValues[3].toIntOrNull() ?: (rawH1 + 2)
                    val rawM2 = match.groupValues[4].ifEmpty { "0" }.toIntOrNull() ?: 0

                    val attachedRoomNum = if (match.groupValues.size > 5 && match.groupValues[5].isNotBlank()) {
                        convertPersianDigitsToEnglish(match.groupValues[5].trim())
                    } else ""

                    val localRoomText = if (attachedRoomNum.isNotBlank() && attachedRoomNum !in IGNORED_ROOM_NUMBERS) {
                        attachedRoomNum
                    } else {
                        roomText
                    }

                    val (startH, startM, endH, endM) = if (rawH1 > rawH2) {
                        Quadruple(rawH2, rawM2, rawH1, rawM1)
                    } else {
                        Quadruple(rawH1, rawM1, rawH2, rawM2)
                    }

                    val (slotKey, duration, mode) = mapToSlot(startH, startM, endH, endM)
                    val formattedStart = String.format("%02d:%02d", startH, startM)
                    val formattedEnd = String.format("%02d:%02d", endH, endM)

                    result.add(
                        ParsedCourse(
                            title = currentTitle,
                            day = dayName,
                            startTime = formattedStart,
                            endTime = formattedEnd,
                            slotKey = slotKey,
                            duration = duration,
                            room = localRoomText,
                            mode = mode,
                            examDate = currentExamDate,
                            examTime = currentExamTime
                        )
                    )
                }
            }
        }

        return result
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

    private fun isDayHeaderLine(line: String): Boolean {
        var dayCount = 0
        for (day in listOf("شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه")) {
            if (line.contains(day)) dayCount++
        }
        return dayCount >= 3
    }

    private fun parseChunk(chunk: String): List<ParsedCourse> {
        val result = mutableListOf<ParsedCourse>()

        val detectedDays = detectDays(chunk)
        if (detectedDays.isEmpty()) return emptyList()

        val timeMatches = TIME_REGEX.findAll(chunk).toList()
        if (timeMatches.isEmpty()) return emptyList()

        val roomText = extractRoom(chunk)
        var titleText = cleanCourseTitle(chunk)
        if (titleText.isBlank()) titleText = "درس جدید"

        val chunkExamDate = extractExamDate(chunk) ?: ""
        val chunkExamTime = extractExamTime(chunk) ?: ""

        for (day in detectedDays) {
            for (match in timeMatches) {
                val rawH1 = match.groupValues[1].toIntOrNull() ?: continue
                val rawM1 = match.groupValues[2].ifEmpty { "0" }.toIntOrNull() ?: 0
                val rawH2 = match.groupValues[3].toIntOrNull() ?: (rawH1 + 2)
                val rawM2 = match.groupValues[4].ifEmpty { "0" }.toIntOrNull() ?: 0

                val attachedRoomNum = if (match.groupValues.size > 5 && match.groupValues[5].isNotBlank()) {
                    convertPersianDigitsToEnglish(match.groupValues[5].trim())
                } else ""

                val localRoomText = if (attachedRoomNum.isNotBlank() && attachedRoomNum !in IGNORED_ROOM_NUMBERS) {
                    attachedRoomNum
                } else {
                    roomText
                }

                val (startH, startM, endH, endM) = if (rawH1 > rawH2) {
                    Quadruple(rawH2, rawM2, rawH1, rawM1)
                } else {
                    Quadruple(rawH1, rawM1, rawH2, rawM2)
                }

                val (slotKey, duration, mode) = mapToSlot(startH, startM, endH, endM)

                val formattedStart = String.format("%02d:%02d", startH, startM)
                val formattedEnd = String.format("%02d:%02d", endH, endM)

                result.add(
                    ParsedCourse(
                        title = titleText,
                        day = day,
                        startTime = formattedStart,
                        endTime = formattedEnd,
                        slotKey = slotKey,
                        duration = duration,
                        room = localRoomText,
                        mode = mode,
                        examDate = chunkExamDate,
                        examTime = chunkExamTime
                    )
                )
            }
        }

        return result
    }

    private fun detectDays(text: String): List<String> {
        val detectedDays = mutableListOf<String>()
        var tempText = text
        for (day in PERSIAN_DAYS) {
            if (tempText.contains(day)) {
                detectedDays.add(day)
                tempText = tempText.replace(day, " ")
            }
        }
        return detectedDays
    }

    private fun extractRoom(text: String): String {
        if (text.isBlank()) return ""

        val norm = normalizeDigitsAndSpacing(text)

        // 1. Forward match: Keyword followed by number/code (e.g. کلاس{101}, کلاس 101, مکان: 101, ک.101)
        val forwardMatch = FORWARD_ROOM_REGEX.find(text) ?: FORWARD_ROOM_REGEX.find(norm)
        if (forwardMatch != null) {
            val rawPrefix = forwardMatch.groupValues[1].replace('ك', 'ک').replace('ي', 'ی').trim()
            val rawNumber = forwardMatch.groupValues[2].trim()
            val normNumber = convertPersianDigitsToEnglish(rawNumber)

            if (normNumber.any { it.isDigit() } && normNumber !in IGNORED_ROOM_NUMBERS) {
                val stdPrefix = when {
                    rawPrefix in listOf("ک", "ک.", "ك", "ك.", "مکان", "مکان برگزاری", "مکان تشکیل", "مکان کلاس") -> "کلاس"
                    rawPrefix.contains("کلاس") || rawPrefix.contains("كلاس") -> "کلاس"
                    rawPrefix == "آز" || rawPrefix == "آز." || rawPrefix == "از" || rawPrefix == "از." -> "آز"
                    rawPrefix.contains("آزمایشگاه") || rawPrefix.contains("آزمايشگاه") -> "آزمایشگاه"
                    rawPrefix.contains("اتاق") -> "اتاق"
                    rawPrefix.contains("تالار") -> "تالار"
                    rawPrefix.contains("سایت") -> "سایت"
                    rawPrefix.contains("سالن") -> "سالن"
                    rawPrefix.contains("کارگاه") -> "کارگاه"
                    rawPrefix.contains("دانشکده") -> "دانشکده"
                    rawPrefix.contains("آمفی") -> "آمفی‌تئاتر"
                    else -> rawPrefix
                }
                return if (stdPrefix == "کلاس") normNumber.trim() else "$stdPrefix $normNumber".trim()
            }
        }

        // 2. Reverse match: Number/code followed by keyword (e.g. {101}کلاس, 101کلاس, 101 کلاس)
        val reverseMatch = REVERSE_ROOM_REGEX.find(text) ?: REVERSE_ROOM_REGEX.find(norm)
        if (reverseMatch != null) {
            val rawNumber = reverseMatch.groupValues[1].trim()
            val rawPrefix = reverseMatch.groupValues[2].replace('ك', 'ک').replace('ي', 'ی').trim()
            val normNumber = convertPersianDigitsToEnglish(rawNumber)

            if (normNumber.any { it.isDigit() } && normNumber !in IGNORED_ROOM_NUMBERS) {
                val stdPrefix = when {
                    rawPrefix in listOf("ک", "ک.", "ك", "ك.") -> "کلاس"
                    rawPrefix.contains("کلاس") || rawPrefix.contains("كلاس") -> "کلاس"
                    rawPrefix.contains("آز") -> "آزمایشگاه"
                    rawPrefix.contains("اتاق") -> "اتاق"
                    rawPrefix.contains("تالار") -> "تالار"
                    rawPrefix.contains("سایت") -> "سایت"
                    rawPrefix.contains("سالن") -> "سالن"
                    rawPrefix.contains("کارگاه") -> "کارگاه"
                    rawPrefix.contains("دانشکده") -> "دانشکده"
                    else -> rawPrefix
                }
                return if (stdPrefix == "کلاس") normNumber.trim() else "$stdPrefix $normNumber".trim()
            }
        }

        // 3. Braced / Bracketed Room Number: e.g. {101}, [101]
        val bracedMatch = BRACED_ROOM_REGEX.find(text) ?: BRACED_ROOM_REGEX.find(norm)
        if (bracedMatch != null) {
            val num = convertPersianDigitsToEnglish(bracedMatch.groupValues[1].trim())
            if (num.length in 2..4 && num !in IGNORED_ROOM_NUMBERS) {
                return num
            }
        }

        // 4. Standalone 3-digit Room Number
        val standaloneMatch = STANDALONE_ROOM_REGEX.find(norm) ?: STANDALONE_ROOM_REGEX.find(text)
        if (standaloneMatch != null) {
            val num = convertPersianDigitsToEnglish(standaloneMatch.groupValues[1].trim())
            if (num.length in 2..4 && num !in IGNORED_ROOM_NUMBERS) {
                return num
            }
        }

        return ""
    }

    private fun convertPersianDigitsToEnglish(text: String): String {
        return text
            .replace('۰', '0').replace('۱', '1').replace('۲', '2').replace('۳', '3').replace('۴', '4')
            .replace('۵', '5').replace('۶', '6').replace('۷', '7').replace('۸', '8').replace('۹', '9')
            .replace('٠', '0').replace('١', '1').replace('٢', '2').replace('٣', '3').replace('٤', '4')
            .replace('٥', '5').replace('٦', '6').replace('٧', '7').replace('٨', '8').replace('٩', '9')
    }

    fun applyPersianHalfSpaceRules(text: String): String {
        if (text.isBlank()) return text
        var result = text

        // Common compound words with half-space
        result = result
            .replace("برنامه نویسی", "برنامه‌نویسی")
            .replace("نرم افزار", "نرم‌افزار")
            .replace("سخت افزار", "سخت‌افزار")
            .replace("پیاده سازی", "پیاده‌سازی")
            .replace("بهینه سازی", "بهینه‌سازی")
            .replace("شبیه سازی", "شبیه‌سازی")
            .replace("مدل سازی", "مدل‌سازی")
            .replace("خودکار سازی", "خودکارسازی")
            .replace("پیش نیاز", "پیش‌نیاز")
            .replace("هم نیاز", "هم‌نیاز")

        // Fix plural suffixes " ها" -> "‌ها", " های" -> "‌های", " هایی" -> "‌هایی"
        val haRegex = Regex("""([\u0621-\u064A\u067E\u0686\u06AF\u06A9\u06CC])\s+ها\b""")
        result = haRegex.replace(result) { match ->
            "${match.groupValues[1]}\u200Cها"
        }

        val hayRegex = Regex("""([\u0621-\u064A\u067E\u0686\u06AF\u06A9\u06CC])\s+های\b""")
        result = hayRegex.replace(result) { match ->
            "${match.groupValues[1]}\u200Cهای"
        }

        val hayeRegex = Regex("""([\u0621-\u064A\u067E\u0686\u06AF\u06A9\u06CC])\s+هایی\b""")
        result = hayeRegex.replace(result) { match ->
            "${match.groupValues[1]}\u200Cهایی"
        }

        return result
    }

    fun fixLamAlefInversion(text: String): String {
        if (text.isBlank()) return text
        var result = text

        val dictionaryFixes = mapOf(
            "اسالم" to "اسلام",
            "معادالت" to "معادلات",
            "اطالعات" to "اطلاعات",
            "احتماالت" to "احتمالات",
            "مقاالت" to "مقالات",
            "انقالب" to "انقلاب",
            "تالش" to "تلاش",
            "مالقات" to "ملاقات",
            "اصالح" to "اصلاح",
            "عالمت" to "علامت",
            "اطالع" to "اطلاع",
            "بالفاصله" to "بلافاصله",
            "امالک" to "املاک",
            "کالس" to "کلاس",
            "ایاالت" to "ایالات",
            "تعامالت" to "تعاملات",
            "مواصالت" to "مواصلات",
            "تشکیلات" to "تشکیلات",
            "اصالحات" to "اصلاحات",
            "اصطالحات" to "اصطلاحات",
            "تحوالت" to "تحولات",
            "تبادالت" to "تبادلات",
            "اختالالت" to "اختلالات",
            "مبتال" to "مبتلا",
            "ابتال" to "ابتلا",
            "غالم" to "غلام",
            "سالم" to "سلام",
            "مبادت" to "مبادلات"
        )

        for ((wrong, right) in dictionaryFixes) {
            result = result.replace(wrong, right)
        }

        val regex = Regex("""([\u0621-\u063A\u0641-\u064A\u067E\u0686\u06AF\u06A9\u06CC])ال([\u0621-\u064A\u067E\u0686\u06AF\u06A9\u06CC])""")
        result = regex.replace(result) { match ->
            "${match.groupValues[1]}لا${match.groupValues[2]}"
        }

        return result
    }

    private fun normalizeDigitsAndSpacing(text: String): String {
        if (text.isBlank()) return text

        val nfkc = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFKC)

        return nfkc
            .replace('ي', 'ی')
            .replace('ى', 'ی')
            .replace('ك', 'ک')
            .replace('ة', 'ه')
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('ٱ', 'ا')
            .replace('۰', '0').replace('۱', '1').replace('۲', '2').replace('۳', '3').replace('۴', '4')
            .replace('۵', '5').replace('۶', '6').replace('۷', '7').replace('۸', '8').replace('۹', '9')
            .replace('٠', '0').replace('١', '1').replace('٢', '2').replace('٣', '3').replace('٤', '4')
            .replace('٥', '5').replace('٦', '6').replace('٧', '7').replace('٨', '8').replace('٩', '9')
            .replace("\u200B", "")
            .replace("\u00A0", " ")
            .replace("\uFEFF", "")
            .replace("\u200E", "")
            .replace("\u200F", "")
            .replace(Regex("""\s*\u200C\s*"""), "\u200C")
            .replace(Regex("""\u200C+"""), "\u200C")
            .replace("یک\u200Cشنبه", "یکشنبه")
            .replace("یک شنبه", "یکشنبه")
            .replace("یک‌شنبه", "یکشنبه")
            .replace("يكشنبه", "یکشنبه")
            .replace("دو\u200Cشنبه", "دوشنبه")
            .replace("دو شنبه", "دوشنبه")
            .replace("سه\u200Cشنبه", "سه‌شنبه")
            .replace("سه شنبه", "سه‌شنبه")
            .replace("سه‌ شنبه", "سه‌شنبه")
            .replace("چهار\u200Cشنبه", "چهارشنبه")
            .replace("چهار شنبه", "چهارشنبه")
            .replace("پنج\u200Cشنبه", "پنجشنبه")
            .replace("پنج شنبه", "پنجشنبه")
    }

    private fun cleanCourseTitle(text: String): String {
        var clean = normalizeDigitsAndSpacing(text)
        clean = fixLamAlefInversion(clean)

        clean = TIME_REGEX.replace(clean, " ")
        clean = FORWARD_ROOM_REGEX.replace(clean, " ")
        clean = REVERSE_ROOM_REGEX.replace(clean, " ")
        clean = BRACED_ROOM_REGEX.replace(clean, " ")
        clean = STANDALONE_ROOM_REGEX.replace(clean, " ")

        for (day in PERSIAN_DAYS) {
            clean = clean.replace(day, " ")
        }

        val keywordsToRemove = listOf(
            "نام درس", "عنوان درس", "درس", "کلاس", "استاد", "مدرس", "واحد", "تئوری", "عملی",
            "زمان", "روز", "ساعت", "شماره و گروه", "شماره گروه", "شماره", "کد درس", "کد گروه",
            "ترم درسی", "نیمسال", "مقطع", "دانشجو", "برنامه هفتگی", "برنامه", "هفتگی",
            "مشخصه", "دانشکده", "تاریخ امتحان", "تاریخ آزمون", "ارائه", "توسط", "توضیحات",
            "گروه", "کدگروه", "تعداد واحد", "تعداد", "ظرفیت", "ثبت نام", "لیست", "گزارش",
            "پیشنیاز", "همنیاز", "پیش نیاز", "هم نیاز", "نظری", "کارگاهی",
            "سمینار", "پروژه", "سیستم جامع گلستان", "دانشگاه"
        )

        for (kw in keywordsToRemove) {
            clean = clean.replace(kw, " ")
        }

        // Remove decimal credit units like 3.0, 2.0, 1.5
        clean = clean.replace(Regex("""\b[0-9]+[./][0-9]+\b"""), " ")

        // Remove 2+ digit numbers (group 01, course codes 12345, year 1402)
        clean = clean.replace(Regex("""\b[0-9]{2,10}\b"""), " ")

        // Remove Golestan composite codes like 01_12345
        clean = clean.replace(Regex("""[0-9]{2}_[0-9]{3,8}"""), " ")

        // Remove leading single/multiple digits (e.g. "1 معادلات دیفرانسیل")
        clean = clean.replace(Regex("""^[0-9]+\s+"""), " ")

        clean = clean.replace(Regex("""[|\-–—:：()،,._\\/{}\[\]]"""), " ")
        clean = clean.replace(Regex("""\s+"""), " ").trim()

        // Remove trailing single digits unless part of valid numbered course name
        val validCourseNumWords = listOf(
            "ریاضی", "فیزیک", "شیمی", "زبان", "برنامه‌نویسی", "برنامه نویسی", "مدار",
            "آزمایشگاه", "ازمایشگاه", "آز", "از", "کارگاه", "زبان عمومی", "تربیت بدنی", "ورزش", "مبانی",
            "اندیشه", "اسلام", "فارسی", "معارف", "ادبیات", "عربی", "آمار", "احتمال", "سیستم", "طراحی"
        )
        val trailingNumMatch = Regex("""^(.*?)\s+([0-9])$""").find(clean.trim())
        if (trailingNumMatch != null) {
            val baseTitle = trailingNumMatch.groupValues[1].trim()
            val hasValidContext = validCourseNumWords.any { baseTitle.contains(it) }
            if (!hasValidContext) {
                clean = baseTitle
            }
        }

        // Apply Persian half-space typography rules (e.g. "داده ها" -> "داده‌ها", "برنامه نویسی" -> "برنامه‌نویسی")
        clean = applyPersianHalfSpaceRules(clean)

        // Clean empty brackets and orphan punctuation
        clean = clean.replace(Regex("""\(\s*\)|\{\s*\}|\[\s*\]"""), " ").trim()
        clean = clean.replace(Regex("""^[\s\-:_.,(){}]+|[\s\-:_.,(){}]+$"""), "").trim()

        return clean
    }

    private fun mapToSlot(startH: Int, startM: Int, endH: Int, endM: Int): Triple<String, Int, String> {
        val startMin = startH * 60 + startM
        val endMin = endH * 60 + endM
        val durationMinutes = (endMin - startMin).coerceAtLeast(45)

        val holdingSlots = listOf(
            "8:30" to (8 * 60 + 30),
            "10:15" to (10 * 60 + 15),
            "12:30" to (12 * 60 + 30),
            "14:15" to (14 * 60 + 15),
            "16:00" to (16 * 60)
        )

        val closestHolding = holdingSlots.minByOrNull { kotlin.math.abs(it.second - startMin) }
        val holdingDiff = if (closestHolding != null) kotlin.math.abs(closestHolding.second - startMin) else 999

        if (holdingDiff <= 20 && (startM == 30 || startM == 15 || startM == 45)) {
            return Triple(closestHolding!!.first, 1, "holding")
        }

        val golestanHour = startH.coerceIn(8, 17)
        val durationHours = ((durationMinutes + 30) / 60).coerceIn(1, 3)

        return Triple(golestanHour.toString(), durationHours, "golestan")
    }

    fun parseGolestanContent(rawText: String, rawHtml: String = ""): List<ParsedCourse> {
        // 1. Try parsing directly using Strategy 2 text parsing
        val coursesFromText = parse(rawText)
        if (coursesFromText.isNotEmpty()) {
            return coursesFromText
        }

        // 2. If html is provided, convert HTML tags to structured text with newlines
        if (rawHtml.isNotBlank()) {
            val structuredText = convertHtmlToStructuredText(rawHtml)
            val coursesFromHtmlText = parse(structuredText)
            if (coursesFromHtmlText.isNotEmpty()) {
                return coursesFromHtmlText
            }
        }

        return emptyList()
    }

    private fun convertHtmlToStructuredText(html: String): String {
        var text = html
        text = text.replace(Regex("""(?i)<tr[^>]*>"""), "\n")
        text = text.replace(Regex("""(?i)<td[^>]*>"""), " ")
        text = text.replace(Regex("""(?i)<th[^>]*>"""), " ")
        text = text.replace(Regex("""(?i)<br\s*/?>"""), "\n")
        text = text.replace(Regex("""(?i)</p>"""), "\n")
        text = text.replace(Regex("""(?i)</div>"""), "\n")
        text = text.replace(Regex("""<[^>]+>"""), "")
        return text.replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
    }

    private val PERSIAN_MONTHS = mapOf(
        "فروردین" to 1, "اردیبهشت" to 2, "خرداد" to 3,
        "تیر" to 4, "مرداد" to 5, "شهریور" to 6,
        "مهر" to 7, "آبان" to 8, "آذر" to 9,
        "دی" to 10, "بهمن" to 11, "اسفند" to 12,
        "فروردين" to 1, "ارديبهشت" to 2, "شهريور" to 6, "آبان" to 8, "آذر" to 9
    )

    fun extractExamDate(text: String): String? {
        if (text.isBlank()) return null

        val norm = convertPersianDigitsToEnglish(text)
            .replace('.', '/')
            .replace('-', '/')
            .replace('\\', '/')
            .replace('_', '/')
            .replace(Regex("""\s*/\s*"""), "/")

        // 1. Persian month name format: e.g. "25 دی 1403" or "1403 دی 25"
        for ((monthName, monthNum) in PERSIAN_MONTHS) {
            if (norm.contains(monthName)) {
                val numRegex = Regex("""\b(\d{1,4})\b""")
                val numbers = numRegex.findAll(norm).map { it.groupValues[1] }.toList()
                if (numbers.size >= 2) {
                    val n1 = numbers[0].toIntOrNull() ?: continue
                    val n2 = numbers[1].toIntOrNull() ?: continue
                    val (year, day) = when {
                        n1 > 1000 -> Pair(n1, n2)
                        n2 > 1000 -> Pair(n2, n1)
                        n1 > 31 -> Pair(if (n1 < 100) 1400 + n1 else n1, n2)
                        else -> Pair(if (n2 < 100) 1400 + n2 else n2, n1)
                    }
                    val parsed = parseDateParts(year.toString(), monthNum.toString(), day.toString())
                    if (parsed != null) return parsed
                }
            }
        }

        // 2. Slash/Dash/Dot-separated 3 numbers: e.g. 1403/10/25 or 25/10/1403 or 03/10/25
        val dateRegex = Regex("""\b(\d{1,4})[./-](\d{1,4})[./-](\d{1,4})\b""")
        val matches = dateRegex.findAll(norm)

        for (match in matches) {
            val p1 = match.groupValues[1]
            val p2 = match.groupValues[2]
            val p3 = match.groupValues[3]

            val parsed = parseDateParts(p1, p2, p3) ?: parseDateParts(p3, p2, p1)
            if (parsed != null) return parsed

            val rev1 = p1.reversed()
            val rev2 = p2.reversed()
            val rev3 = p3.reversed()

            val parsedRev = parseDateParts(rev1, rev2, rev3) ?: parseDateParts(rev3, rev2, rev1)
            if (parsedRev != null) return parsedRev
        }

        // 3. Fallback: space-separated numbers: e.g. "1403 10 25"
        val spaceDateRegex = Regex("""\b(\d{1,4})\s+(\d{1,4})\s+(\d{1,4})\b""")
        val spaceMatches = spaceDateRegex.findAll(norm)
        for (match in spaceMatches) {
            val p1 = match.groupValues[1]
            val p2 = match.groupValues[2]
            val p3 = match.groupValues[3]

            val parsed = parseDateParts(p1, p2, p3) ?: parseDateParts(p3, p2, p1)
            if (parsed != null) return parsed
        }

        return null
    }

    private fun parseDateParts(yearStr: String, monthStr: String, dayStr: String): String? {
        val rawYear = yearStr.toIntOrNull() ?: return null
        val month = monthStr.toIntOrNull() ?: return null
        val day = dayStr.toIntOrNull() ?: return null

        val year = if (rawYear in 0..30) 1400 + rawYear else rawYear

        val validYear = year in 1390..1420
        val validMonth = month in 1..12
        val validDay = day in 1..31

        if (validYear && validMonth && validDay) {
            return String.format(Locale.US, "%04d/%02d/%02d", year, month, day)
        }
        return null
    }

    fun extractExamTime(text: String): String? {
        if (text.isBlank()) return null

        val norm = convertPersianDigitsToEnglish(text)

        if (extractExamDate(text) != null && !text.contains(":") && !text.contains("تا") && !text.contains("-")) {
            return null
        }

        val rangeRegex = Regex("""\b([012]?\d)[：:.]?(\d{2})?\s*(?:تا|-|–|—|الی|الى|_|~|to)\s*([012]?\d)[：:.]?(\d{2})?\b""")
        val match = rangeRegex.find(norm)

        if (match != null) {
            val rawH1 = match.groupValues[1].toIntOrNull() ?: return null
            val rawM1 = match.groupValues[2].ifEmpty { "0" }.toIntOrNull() ?: 0
            val rawH2 = match.groupValues[3].toIntOrNull() ?: return null
            val rawM2 = match.groupValues[4].ifEmpty { "0" }.toIntOrNull() ?: 0

            if (rawH1 in 0..23 && rawH2 in 0..23 && rawM1 in 0..59 && rawM2 in 0..59) {
                val (startH, startM) = if (rawH1 * 60 + rawM1 > rawH2 * 60 + rawM2) {
                    Pair(rawH2, rawM2)
                } else {
                    Pair(rawH1, rawM1)
                }
                return String.format(Locale.US, "%02d:%02d", startH, startM)
            }
        }

        // Fallback: single time
        val singleTimeRegex = Regex("""\b([012]?\d)[：:.](\d{2})\b""")
        val singleMatch = singleTimeRegex.find(norm)
        if (singleMatch != null) {
            val h = singleMatch.groupValues[1].toIntOrNull() ?: return null
            val m = singleMatch.groupValues[2].toIntOrNull() ?: 0
            if (h in 0..23 && m in 0..59) {
                return String.format(Locale.US, "%02d:%02d", h, m)
            }
        }

        return null
    }

    private fun deduplicateCourses(courses: List<ParsedCourse>): List<ParsedCourse> {
        val unique = mutableListOf<ParsedCourse>()
        for (c in courses) {
            val exists = unique.any {
                it.day == c.day && it.slotKey == c.slotKey && it.title == c.title
            }
            if (!exists && c.title.isNotBlank()) {
                unique.add(c)
            }
        }
        return unique
    }
}
