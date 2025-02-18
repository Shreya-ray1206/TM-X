package org.kibbcom.tm_x.platform

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.location.LocationManager
import android.os.Build
import org.kibbcom.tm_x.AppContextProvider
import android.content.pm.PackageManager
actual class PlatformUtils {

    private val context : Context by lazy {
        AppContextProvider.getContext()
    }

    actual fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter?.isEnabled == true
    }

    actual fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    actual fun isAndroid(): Boolean = true

    actual fun getAndroidVersion(): Int = Build.VERSION.SDK_INT


    actual fun getAppVersion(): String {
            return try {
                val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                packageInfo.versionName ?: "Unknown"
            } catch (e: PackageManager.NameNotFoundException) {
                "Unknown"
            }
    }

}