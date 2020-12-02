package com.archrahkshi.spotifine

import android.app.Application
import android.os.StrictMode

class App : Application() { // Some weird warning which is certainly false
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads() - Not detecting disk reads cuz Xiaomi ¯\_(ツ)_/¯
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }
    }
}
