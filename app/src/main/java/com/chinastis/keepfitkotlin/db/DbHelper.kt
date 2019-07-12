package com.chinastis.keepfitkotlin.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by xianglong on 2019/7/12.
 */
class DbHelper(context: Context, name:String, factory:SQLiteDatabase.CursorFactory, version:Int) :
        SQLiteOpenHelper(context,name,factory,version) {


    override fun onCreate(db: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}