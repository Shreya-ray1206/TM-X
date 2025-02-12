package org.kibbcom.tm_x

import platform.Foundation.NSBundle

actual fun getImageResourcePath(imageName: String): String {
    return NSBundle.mainBundle.pathForResource(imageName, "png") ?: ""
}