package org.kibbcom.tm_x

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform