package com.example.myuni

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.edit
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GolestanAutoLoginDialog(
    themeColor: Color = MaterialTheme.colorScheme.primary,
    onCoursesExtracted: (List<ParsedCourse>) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("MyUniPrefs", android.content.Context.MODE_PRIVATE)

    var username by remember { mutableStateOf(prefs.getString("golestan_saved_user", "") ?: "") }
    var password by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) } // 1: Login Form, 2: Automated Fetching
    var statusMessage by remember { mutableStateOf("در حال اتصال به سامانه گلستان...") }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    fun processExtractedData(rawText: String, rawHtml: String) {
        val courses = CourseScheduleParser.parseGolestanContent(rawText, rawHtml)
        if (courses.isNotEmpty()) {
            Toast.makeText(context, "تعداد ${courses.size} درس با موفقیت دریافت شد!", Toast.LENGTH_LONG).show()
            webViewRef?.clearCache(true)
            webViewRef?.clearHistory()
            onCoursesExtracted(courses)
        } else {
            statusMessage = "وارد سامانه شدید. لطفاً در صفحه باز شده به گزارش ۷۸ بروید تا برنامه استخراج شود."
            step = 2
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (step == 1) 24.dp else 8.dp),
            shape = RoundedCornerShape(AppConfig.dialogCornerRadius),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            if (step == 1) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ورود خودکار به سامانه گلستان",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = themeColor,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("شماره دانشجویی (نام کاربری)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = themeColor) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("رمز عبور گلستان") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = themeColor) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (username.isBlank() || password.isBlank()) {
                                Toast.makeText(context, "لطفاً نام کاربری و رمز عبور را وارد کنید", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            prefs.edit { putString("golestan_saved_user", username) }
                            step = 2
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("ورود و دریافت خودکار برنامه", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onDismiss) {
                        Text("لغو", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(
                        color = themeColor.copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = statusMessage, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            TextButton(onClick = { step = 1 }) {
                                Text("تغییر مشخصات")
                            }
                            TextButton(onClick = onDismiss) {
                                Text("بستن")
                            }
                        }
                    }

                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = themeColor)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                WebView(ctx).apply {
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    settings.allowFileAccess = false
                                    settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                    settings.setSupportMultipleWindows(true)
                                    settings.javaScriptCanOpenWindowsAutomatically = true

                                    android.webkit.CookieManager.getInstance().setAcceptCookie(true)
                                    android.webkit.CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

                                    webChromeClient = object : android.webkit.WebChromeClient() {
                                        override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: android.os.Message?): Boolean {
                                            val transport = resultMsg?.obj as? WebView.WebViewTransport
                                            transport?.webView = view
                                            resultMsg?.sendToTarget()
                                            return true
                                        }
                                    }

                                    webViewClient = object : WebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            val currentUrl = url ?: ""

                                            if (currentUrl.contains("authenticateuser")) {
                                                statusMessage = "در حال ورود به سامانه گلستان..."
                                                val js = """
                                                    (function() {
                                                        var txts = document.querySelectorAll('input[type="text"], input:not([type])');
                                                        var passes = document.querySelectorAll('input[type="password"]');
                                                        for(var i=0; i<txts.length; i++) {
                                                            if(txts[i].id.toLowerCase().includes('usr') || txts[i].name.toLowerCase().includes('usr') || txts[i].id.startsWith('D00')) {
                                                                txts[i].value = "$username";
                                                            }
                                                        }
                                                        if(passes.length > 0) {
                                                            passes[0].value = "$password";
                                                        }
                                                    })();
                                                """.trimIndent()
                                                view?.evaluateJavascript(js, null)
                                            } else {
                                                statusMessage = "ورود موفق! در حال دریافت برنامه درسی..."
                                                view?.evaluateJavascript(
                                                    """
                                                    (function() {
                                                        var text = document.body.innerText || "";
                                                        var hasSchedule = (text.includes("برنامه") || text.includes("گزارش") || text.includes("واحد")) && (text.includes("شنبه") || text.includes("یکشنبه"));
                                                        if (hasSchedule) {
                                                            return JSON.stringify({
                                                                text: text,
                                                                html: document.body.innerHTML || ""
                                                            });
                                                        }
                                                        return "";
                                                    })();
                                                    """.trimIndent()
                                                ) { jsonResult ->
                                                    if (!jsonResult.isNullOrBlank() && jsonResult != "null" && jsonResult != "\"\"") {
                                                        try {
                                                            val jsonString = if (jsonResult.startsWith("\"") && jsonResult.endsWith("\"")) {
                                                                JSONObject("{\"val\": $jsonResult}").getString("val")
                                                            } else {
                                                                jsonResult
                                                            }
                                                            val obj = JSONObject(jsonString)
                                                            val txt = obj.optString("text", "")
                                                            val html = obj.optString("html", "")
                                                            processExtractedData(txt, html)
                                                        } catch (_: Exception) {}
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    loadUrl("https://golestan.ui.ac.ir/forms/authenticateuser/main.htm")
                                    webViewRef = this
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
