package org.kibbcom.tm_x



import android.content.Context

object ContextUtils {
    lateinit var context: Context
        private set

    fun initialize(context: Context) {
        this.context = context
    }
}