package com.flopezluksenberg.todo

import android.app.Application
import com.squareup.leakcanary.LeakCanary


class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) { return }
        LeakCanary.install(this)
    }

}