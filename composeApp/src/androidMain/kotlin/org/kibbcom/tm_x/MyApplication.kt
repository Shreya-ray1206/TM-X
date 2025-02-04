package org.kibbcom.tm_x

import android.app.Application

class MyApplication :  Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextProvider.init(this)
    }
}