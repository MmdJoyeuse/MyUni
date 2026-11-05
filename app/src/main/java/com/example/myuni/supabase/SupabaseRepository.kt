package com.example.myuni.supabase

import android.content.Context
import android.os.Build
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object SupabaseRepository {

    private const val PREFS_NAME = "myuni_supabase_prefs"
    private const val KEY_USER_UUID = "user_uuid"

    fun getUserUuid(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var uuid = prefs.getString(KEY_USER_UUID, null)
        if (uuid.isNullOrEmpty()) {
            uuid = UUID.randomUUID().toString()
            prefs.edit().putString(KEY_USER_UUID, uuid).apply()
        }
        return uuid
    }

    fun getAppVersionName(context: Context): String {
        return runCatching {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "1.0.0"
        }.getOrDefault("1.0.0")
    }

    /**
     * ثبت مشخصات کاربر جدید و یا بروزرسانی تاریخ آخرین ورود (last_seen) در سوپابیس
     */
    suspend fun registerOrUpdateUser(context: Context): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val uuid = getUserUuid(context)
            val deviceInfo = "${Build.MANUFACTURER} ${Build.MODEL}"
            val androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
            val appVersion = getAppVersionName(context)

            val profile = UserProfile(
                id = uuid,
                deviceInfo = deviceInfo,
                appVersion = appVersion,
                androidVersion = androidVersion,
                lastSeen = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
            )

            SupabaseConfig.client.postgrest["profiles"].upsert(profile)
            Unit
        }
    }

    /**
     * دریافت تنظیمات عمومی از سوپابیس (هفته زوج/فرد و وضعیت جدیدترین نسخه اپلیکیشن)
     */
    suspend fun fetchAppConfig(): Result<AppConfigRemote?> = withContext(Dispatchers.IO) {
        runCatching {
            SupabaseConfig.client.postgrest["app_config"]
                .select()
                .decodeSingleOrNull<AppConfigRemote>()
        }
    }
}
