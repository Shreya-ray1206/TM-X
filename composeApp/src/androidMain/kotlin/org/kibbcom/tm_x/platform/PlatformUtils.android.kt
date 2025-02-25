package org.kibbcom.tm_x.platform

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import org.kibbcom.tm_x.AppContextProvider
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import kotlinx.io.IOException
import org.kibbcom.tm_x.LogEntry
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

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

    actual fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Required when using application context
        }
        context.startActivity(intent)
    }

    actual fun saveCsvFile(
        fileName: String,
        logs: List<LogEntry>
    ): String {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        FileWriter(file).use { writer ->
            writer.append("ID,Message,Date\n") // CSV header
            logs.forEach { log ->
                writer.append("${log.id},\"${log.message}\",${log.date}\n")
            }
        }

        return file.absolutePath



    }

    actual fun savePdfFile(
        fileName: String,
        logs: List<LogEntry>
    ): String {

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size: 595x842 points
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = android.graphics.Paint()
        paint.textSize = 16f

        var y = 50f // Start Y position
        canvas.drawText("Logs Report", 200f, y, paint) // Title
        y += 40f

        // Draw log entries
        logs.forEach { log ->
            canvas.drawText("ID: ${log.id}, Message: ${log.message}, Date: ${log.date}", 50f, y, paint)
            y += 30f
        }

        pdfDocument.finishPage(page)

        try {
            FileOutputStream(file).use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "Error: ${e.message}"
        } finally {
            pdfDocument.close()
        }

        return file.absolutePath
    }

}