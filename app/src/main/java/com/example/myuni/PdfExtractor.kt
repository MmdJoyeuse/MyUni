package com.example.myuni

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import com.tom_roush.pdfbox.text.TextPosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class PdfTextItem(
    val text: String,
    val x: Float,
    val y: Float
)

data class PdfExtractionResult(
    val fileName: String,
    val pageCount: Int,
    val rawText: String,
    val formattedText: String,
    val textItems: List<PdfTextItem> = emptyList(),
    val errorMessage: String? = null,
)

object PdfExtractor {

    suspend fun extractText(context: Context, uri: Uri): PdfExtractionResult = withContext(Dispatchers.IO) {
        val fileName = getFileName(context, uri) ?: "فایل PDF"
        try {
            PDFBoxResourceLoader.init(context.applicationContext)
            
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return@withContext PdfExtractionResult(fileName, 0, "", "", emptyList(), "امکان باز کردن فایل PDF وجود ندارد.")

            val text: String
            val pageCount: Int
            val items = mutableListOf<PdfTextItem>()

            inputStream.use { stream ->
                PDDocument.load(stream).use { document ->
                    pageCount = document.numberOfPages
                    val stripper = object : PDFTextStripper() {
                        init {
                            sortByPosition = true
                        }

                        override fun writeString(text: String, textPositions: MutableList<TextPosition>) {
                            super.writeString(text)
                            val firstPos = textPositions.firstOrNull()
                            if (firstPos != null) {
                                items.add(PdfTextItem(text = text, x = firstPos.xDirAdj, y = firstPos.yDirAdj))
                            }
                        }
                    }
                    text = stripper.getText(document) ?: ""
                }
            }

            val cleanedText = normalizePersianText(text)

            PdfExtractionResult(
                fileName = fileName,
                pageCount = pageCount,
                rawText = text,
                formattedText = cleanedText,
                textItems = items
            )
        } catch (e: Exception) {
            e.printStackTrace()
            PdfExtractionResult(
                fileName = fileName,
                pageCount = 0,
                rawText = "",
                formattedText = "",
                textItems = emptyList(),
                errorMessage = "خطا در پردازش فایل PDF: ${e.localizedMessage ?: "فایل معتبر یا قابل خواندن نیست."}"
            )
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = cursor.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            val path = uri.path
            val cut = path?.lastIndexOf('/') ?: -1
            if (cut != -1 && path != null) {
                result = path.substring(cut + 1)
            }
        }
        return result
    }

    fun normalizePersianText(text: String): String {
        if (text.isBlank()) return text

        // 1. NFKC Unicode normalization (converts Arabic Presentation Forms A & B to standard Persian Unicode characters)
        val nfkc = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFKC)

        // 2. Normalize Arabic letter variants to standard Persian
        var cleaned = nfkc
            .replace('ي', 'ی')
            .replace('ى', 'ی')
            .replace('ك', 'ک')
            .replace('ة', 'ه')
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('ٱ', 'ا')
            .replace('٠', '۰').replace('١', '۱').replace('٢', '۲').replace('٣', '۳').replace('٤', '۴')
            .replace('٥', '۵').replace('٦', '۶').replace('٧', '۷').replace('٨', '۸').replace('٩', '۹')

        // 3. Fix inverted Lam-Alef (اسالم -> اسلام, معادالت -> معادلات)
        cleaned = CourseScheduleParser.fixLamAlefInversion(cleaned)

        // 4. Preserve half-space (\u200C) and remove invisible control chars
        cleaned = cleaned
            .replace("\u200B", "")
            .replace("\uFEFF", "")
            .replace("\u200E", "")
            .replace("\u200F", "")
            .replace("\u061C", "")
            .replace(Regex("""\s*\u200C\s*"""), "\u200C")
            .replace(Regex("""\u200C+"""), "\u200C")

        // 5. Apply Persian half-space typography rules
        cleaned = CourseScheduleParser.applyPersianHalfSpaceRules(cleaned)

        // 6. Normalize common day names & typos
        cleaned = cleaned
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

        return cleaned
    }

    fun reverseLines(text: String): String {
        return text.lines().joinToString("\n") { line ->
            line.reversed()
        }
    }
}
