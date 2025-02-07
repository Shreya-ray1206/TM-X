package org.kibbcom.tm_x


sealed class Screen {
    object Device : Screen(){
        override fun toString(): String = "Device"
    }
    object Beacon : Screen(){
        override fun toString(): String = "Beacon"
    }
    object BleScanning : Screen(){
        override fun toString(): String = "Scanning"
    }
    object Settings : Screen(){
        override fun toString(): String = "Settings"
    }
  object Permission : Screen(){
        override fun toString(): String = "Permission"
    }
    object DummyScreen : Screen(){
        override fun toString(): String = "DummyScreen"
    }

}