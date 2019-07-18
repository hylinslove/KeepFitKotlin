package com.chinastis.keepfitkotlin

import android.app.Application
import android.content.ContentValues
import android.content.Context

import com.chinastis.keepfitkotlin.db.DbManager

import java.util.ArrayList
import java.util.HashMap

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        val dbManager = DbManager.getInstance(this)
        val list = dbManager.select("select * from user")

        if (list.size == 0) {
            val cv = ContentValues()
            cv.put("user_id", "0000")
            cv.put("user_name", "default_user")
            dbManager.inert("user", cv)
        }
    }

    companion object {
        private var appContext: Context? = null
    }


}
