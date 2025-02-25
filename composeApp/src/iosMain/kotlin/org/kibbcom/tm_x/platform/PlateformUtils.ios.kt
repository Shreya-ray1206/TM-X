package org.kibbcom.tm_x.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import org.kibbcom.tm_x.LogEntry
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSDictionary
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSMutableDictionary
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithBytes
import platform.UIKit.NSFontAttributeName
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UIGraphicsBeginPDFContextToFile
import platform.UIKit.UIGraphicsBeginPDFPage
import platform.UIKit.UIGraphicsEndPDFContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsPDFRenderer
import platform.UIKit.drawAtPoint
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

    actual fun saveCsvFile(
        fileName: String,
        logs: List<LogEntry>
    ): String {

        val documentDirectory = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, true
        ).firstOrNull() ?: return "Error: No document directory found"

        val filePath = "$documentDirectory/$fileName"

        // Convert logs to CSV format string
        val fileContent = buildString {
            append("ID,Message,Date\n") // CSV Header
            logs.forEach { log ->
                append("${log.id},\"${log.message}\",${log.date}\n")
            }
        }

        // Convert String to NSData safely
        val fileData: NSData? = fileContent.encodeToByteArray().toNSData()

        // Write file to storage
        if (fileData != null) {
            NSFileManager.defaultManager.createFileAtPath(filePath, fileData, null)
        }

        return filePath
    }


    @OptIn(ExperimentalForeignApi::class)
    fun ByteArray.toNSData(): NSData? {
        return this.usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), this.size.toULong())
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun savePdfFile(
        fileName: String,
        logs: List<LogEntry>
    ): String {
        val documentDirectory = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory, NSUserDomainMask, true
        ).firstOrNull() ?: return "Error: No document directory found"

        val filePath = "$documentDirectory/$fileName"

        val pageWidth = 595.0  // A4 width in points
        val pageHeight = 842.0 // A4 height in points

        UIGraphicsBeginPDFContextToFile(filePath, CGRectMake(0.0, 0.0, pageWidth, pageHeight), null)
        UIGraphicsBeginPDFPage()

        val font = UIFont.systemFontOfSize(16.0)
        val textColor = UIColor.blackColor

        var yPosition = 50.0 // Starting Y position
        drawText("Logs Report", 200.0, yPosition, font, textColor)
        yPosition += 40.0

        logs.forEach { log ->
            val logText = "ID: ${log.id}, Message: ${log.message}, Date: ${log.date}"
            drawText(logText, 50.0, yPosition, font, textColor)
            yPosition += 30.0
        }

        UIGraphicsEndPDFContext()

        return filePath
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun drawText(text: String, x: Double, y: Double, font: UIFont, color: UIColor) {
        val attributes = NSMutableDictionary()
        attributes.setObject(font, forKey = NSFontAttributeName as NSString)
        attributes.setObject(color, forKey = NSForegroundColorAttributeName as NSString)

        val nsText = NSString.create(string = text)

        //todo ios fixes
       /* nsText.drawAtPoint(
            point = CGPointMake(x, y),
            withAttributes = attributes
        )*/
    }
}