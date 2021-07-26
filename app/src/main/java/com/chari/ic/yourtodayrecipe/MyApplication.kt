package com.chari.ic.yourtodayrecipe

import android.app.Application
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication: Application() {
//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onCreate() {
//        super.onCreate()
//        StrictMode.setVmPolicy(
//            VmPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .detectNonSdkApiUsage()
//                .build()
//        )
//    }
}