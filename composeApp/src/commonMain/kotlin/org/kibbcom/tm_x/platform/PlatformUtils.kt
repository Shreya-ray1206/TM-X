package org.kibbcom.tm_x.platform

expect class PlatformUtils() {
    fun isAndroid(): Boolean
    fun getAndroidVersion(): Int
    fun isBluetoothEnabled(): Boolean
    fun isLocationEnabled(): Boolean // Only needed for Android, can return true on iOS
}
