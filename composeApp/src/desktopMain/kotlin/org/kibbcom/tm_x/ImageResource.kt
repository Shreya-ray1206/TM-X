package org.kibbcom.tm_x

actual fun getImageResourcePath(imageName: String): String {
    return "file://path/to/resources/$imageName.png"
}