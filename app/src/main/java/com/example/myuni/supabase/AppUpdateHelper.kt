package com.example.myuni.supabase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object AppUpdateHelper {

    fun openMarket(context: Context, url: String?) {
        try {
            val targetUrl = if (url.isNullOrBlank()) {
                "https://cafebazaar.ir/app/${context.packageName}"
            } else {
                url
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "خطا در باز کردن لینک: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}
