package org.kibbcom.tm_x.platform

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
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

}