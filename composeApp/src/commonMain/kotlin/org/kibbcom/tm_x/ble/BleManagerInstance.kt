package org.kibbcom.tm_x.ble

import org.kibbcom.tm_x.BleManager

class BleManagerInstance {
    val instance: BleManager by lazy { BleManager() }
}