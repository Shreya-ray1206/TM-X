package org.kibbcom.helloworld

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform