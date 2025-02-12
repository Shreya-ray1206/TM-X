package org.kibbcom.tm_x

actual fun getImageResourcePath(imageName: String): String {
    return "android.resource://${ContextUtils.context.packageName}/drawable/$imageName"
}