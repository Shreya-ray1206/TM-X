package org.kibbcom.tm_x.platform

import org.kibbcom.tm_x.LogEntry

expect class PlatformUtils() {
    fun isAndroid(): Boolean
    fun getAndroidVersion(): Int
    fun getAppVersion(): String
    fun isBluetoothEnabled(): Boolean
    fun isLocationEnabled(): Boolean // Only needed for Android, can return true on iOS
    fun openUrl(url: String)
    fun saveCsvFile(fileName: String, logs: List<LogEntry>): String
    fun savePdfFile(fileName: String, logs: List<LogEntry>): String

}
