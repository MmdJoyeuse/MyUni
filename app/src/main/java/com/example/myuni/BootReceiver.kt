package com.example.myuni

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == "android.intent.action.QUICKBOOT_POWERON" ||
            action == "com.htc.intent.action.QUICKBOOT_POWERON" ||
            action == Intent.ACTION_LOCKED_BOOT_COMPLETED
        ) {
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
    }
}
