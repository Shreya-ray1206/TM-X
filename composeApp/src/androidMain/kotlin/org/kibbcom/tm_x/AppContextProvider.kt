package org.kibbcom.tm_x

import android.content.Context

object AppContextProvider {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getContext(): Context {
        return appContext ?: throw IllegalStateException("Context not initialized")
    }
}