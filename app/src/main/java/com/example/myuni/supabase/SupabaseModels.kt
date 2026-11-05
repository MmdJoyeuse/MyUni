package com.example.myuni.supabase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("id")
    val id: String,
    @SerialName("device_info")
    val deviceInfo: String? = null,
    @SerialName("app_version")
    val appVersion: String? = null,
    @SerialName("android_version")
    val androidVersion: String? = null,
    @SerialName("last_seen")
    val lastSeen: String? = null
)

@Serializable
data class AppConfigRemote(
    @SerialName("id")
    val id: Int = 1,
    @SerialName("is_week_flipped")
    val isWeekFlipped: Boolean = false,
    @SerialName("latest_version_code")
    val latestVersionCode: Int = 1,
    @SerialName("latest_version_name")
    val latestVersionName: String = "1.0.0",
    @SerialName("download_url")
    val downloadUrl: String? = null,
    @SerialName("release_notes")
    val releaseNotes: String? = null,
    @SerialName("force_update")
    val forceUpdate: Boolean = false
)
