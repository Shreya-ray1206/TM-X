package org.kibbcom.tm_x.platform

actual class PlatformUtils {
    actual fun isBluetoothEnabled(): Boolean {
      return true
    }

    actual fun isLocationEnabled(): Boolean {
        return true
    }
    actual fun isAndroid(): Boolean = false

    actual fun getAndroidVersion(): Int = 0
    actual fun getAppVersion(): String {
        return "0"
    }
}