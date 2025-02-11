package org.kibbcom.tm_x.platform

actual class PlatformUtils {
    actual fun isBluetoothEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun isLocationEnabled(): Boolean {
        return true
    }
    actual fun isAndroid(): Boolean = false

    actual fun getAndroidVersion(): Int = 0 // Not applicable for iOS
}