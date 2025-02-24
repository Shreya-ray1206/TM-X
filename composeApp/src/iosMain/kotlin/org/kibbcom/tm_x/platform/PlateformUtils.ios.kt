package org.kibbcom.tm_x.platform

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.darwin.NSObject

actual class PlatformUtils : NSObject() {
    private val centralManager = CBCentralManager()

    actual fun isBluetoothEnabled(): Boolean {
        return true
    }

    actual fun isLocationEnabled(): Boolean {
        return true
    }
    actual fun isAndroid(): Boolean = false

    actual fun getAndroidVersion(): Int = 0 // Not applicable for iOS
    actual fun getAppVersion(): String {
        return NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "Unknown"
    }

    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

}