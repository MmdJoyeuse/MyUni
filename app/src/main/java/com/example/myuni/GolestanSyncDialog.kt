package com.example.myuni

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GolestanSyncDialog(
    initialUrl: String = "https://golestan.ui.ac.ir/forms/authenticateuser/main.htm",
    themeColor: Color = MaterialTheme.colorScheme.primary,
    onCoursesExtracted: (List<ParsedCourse>) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }

    fun extractAndParseSchedule() {
        webViewRef?.evaluateJavascript(
            """
            (function() {
                function getAllFramesText(win) {
                    var fullText = "";
                    var fullHtml = "";
                    try {
                        if (win.document && win.document.documentElement) {
                            fullText += (win.document.body ? win.document.body.innerText : "") + "\n";
                            fullHtml += (win.document.documentElement.outerHTML || "") + "\n";
                        }
                        var frames = win.frames;
                        for (var i = 0; i < frames.length; i++) {
                            try {
                                var res = getAllFramesText(frames[i]);
                                fullText += res.text + "\n";
                                fullHtml += res.html + "\n";
                            } catch (e) {}
                        }
                    } catch (e) {}
                    return { text: fullText, html: fullHtml };
                }
                var res = getAllFramesText(window);
                return JSON.stringify({
                    text: res.text,
                    html: res.html
                });
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

                    val courses = CourseScheduleParser.parseGolestanContent(txt, html)
                    if (courses.isNotEmpty()) {
                        Toast.makeText(context, "تعداد ${courses.size} درس با موفقیت استخراج و ثبت شد!", Toast.LENGTH_LONG).show()
                        webViewRef?.clearCache(true)
                        webViewRef?.clearFormData()
                        webViewRef?.clearHistory()
                        onCoursesExtracted(courses)
                    } else {
                        Toast.makeText(context, "درسی یافت نشد. لطفاً مطمئن شوید در صفحه گزارش برنامه درسی هستید.", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "خطا در پردازش اطلاعات: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "اطلاعاتی از صفحه دریافت نشد.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Dialog(
        onDismissRequest = {
            webViewRef?.clearCache(true)
            webViewRef?.clearFormData()
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            shape = RoundedCornerShape(AppConfig.dialogCornerRadius),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Navigation Bar
                Surface(
                    color = themeColor.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        IconButton(
                            onClick = {
                                webViewRef?.clearCache(true)
                                webViewRef?.clearFormData()
                                onDismiss()
                            }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "بستن")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { webViewRef?.reload() }
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "بارگذاری مجدد")
                            }

                            if (canGoBack) {
                                IconButton(
                                    onClick = { webViewRef?.goBack() }
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "صفحه قبل")
                                }
                            }

                            if (canGoForward) {
                                IconButton(
                                    onClick = { webViewRef?.goForward() }
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "صفحه بعد")
                                }
                            }
                        }
                    }
                }

                // Loading Indicator
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = themeColor
                    )
                }

                // WebView Container
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    allowFileAccess = false
                                    saveFormData = false
                                    setSupportZoom(true)
                                    builtInZoomControls = true
                                    displayZoomControls = false
                                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                    setSupportMultipleWindows(true)
                                    javaScriptCanOpenWindowsAutomatically = true
                                }

                                CookieManager.getInstance().setAcceptCookie(true)
                                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

                                webChromeClient = object : WebChromeClient() {
                                    override fun onCreateWindow(
                                        view: WebView?,
                                        isDialog: Boolean,
                                        isUserGesture: Boolean,
                                        resultMsg: android.os.Message?
                                    ): Boolean {
                                        try {
                                            val cxt = view?.context ?: return false
                                            val newWebView = WebView(cxt)
                                            newWebView.webViewClient = object : WebViewClient() {
                                                override fun shouldOverrideUrlLoading(
                                                    view: WebView?,
                                                    request: WebResourceRequest?
                                                ): Boolean {
                                                    request?.url?.toString()?.let { webViewRef?.loadUrl(it) }
                                                    return true
                                                }
                                                @Suppress("DEPRECATION")
                                                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                                    url?.let { webViewRef?.loadUrl(it) }
                                                    return true
                                                }
                                            }
                                            val transport = resultMsg?.obj as? WebView.WebViewTransport
                                            if (transport != null) {
                                                transport.webView = newWebView
                                                resultMsg.sendToTarget()
                                                return true
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        return false
                                    }

                                    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: android.webkit.JsResult?): Boolean {
                                        result?.confirm()
                                        return true
                                    }

                                    override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: android.webkit.JsResult?): Boolean {
                                        result?.confirm()
                                        return true
                                    }

                                    override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: android.webkit.JsPromptResult?): Boolean {
                                        result?.confirm(defaultValue)
                                        return true
                                    }
                                }

                                webViewClient = object : WebViewClient() {
                                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                        super.onPageStarted(view, url, favicon)
                                        isLoading = true
                                        canGoBack = view?.canGoBack() == true
                                        canGoForward = view?.canGoForward() == true
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        isLoading = false
                                        canGoBack = view?.canGoBack() == true
                                        canGoForward = view?.canGoForward() == true

                                        // Neutralize onbeforeunload and confirm dialogs across all frames
                                        view?.evaluateJavascript(
                                            """
                                            (function() {
                                                try {
                                                    window.onbeforeunload = null;
                                                    window.confirm = function() { return true; };
                                                    window.alert = function() {};
                                                    window.prompt = function() { return ""; };
                                                    
                                                    function disableUnload(win) {
                                                        try {
                                                            win.onbeforeunload = null;
                                                            win.confirm = function() { return true; };
                                                            var frames = win.frames;
                                                            for(var i=0; i<frames.length; i++) {
                                                                disableUnload(frames[i]);
                                                            }
                                                        } catch(e) {}
                                                    }
                                                    disableUnload(window);
                                                } catch(e) {}
                                            })();
                                            """.trimIndent(),
                                            null
                                        )
                                    }

                                    override fun onReceivedSslError(
                                        view: WebView?,
                                        handler: SslErrorHandler?,
                                        error: android.net.http.SslError?
                                    ) {
                                        handler?.proceed()
                                    }

                                    override fun onFormResubmission(view: WebView?, dontResend: android.os.Message?, resend: android.os.Message?) {
                                        resend?.sendToTarget()
                                    }
                                }

                                loadUrl(initialUrl)
                                webViewRef = this
                            }
                        },
                        update = { view ->
                            webViewRef = view
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Bottom Extraction Button
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Button(
                            onClick = {
                                extractAndParseSchedule()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = "استخراج برنامه",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
