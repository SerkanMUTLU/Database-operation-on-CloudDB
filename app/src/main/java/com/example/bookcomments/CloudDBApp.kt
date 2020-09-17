package com.example.bookcomments

import android.app.Application
import com.huawei.agconnect.cloud.database.AGConnectCloudDB

class CloudDBApp:Application() {

    override fun onCreate() {
        super.onCreate()

        //TODO  initialize AGConnectCloudDB
        //CloudDB's initialize method is called, this method is called inside manifest file.
        AGConnectCloudDB.initialize(applicationContext)
    }
}