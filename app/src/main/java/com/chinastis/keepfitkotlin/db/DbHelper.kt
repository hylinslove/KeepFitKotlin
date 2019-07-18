package com.chinastis.keepfitkotlin.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by xianglong on 2019/7/12.
 */
class DbHelper(context: Context, name:String, factory:SQLiteDatabase.CursorFactory?, version:Int) :
        SQLiteOpenHelper(context,name,factory,version) {

    companion object {
        val dbName = "keep.db"
        val dbVersion = 1
    }



    override fun onCreate(db: SQLiteDatabase?) {
        val createUserSql = "create table if not exists user(user_id text primary key," +
                "user_name text,goal_weight text,height text)"

       val createWeightSql = "create table if not exists weight(user_id text," +
               "date_time long,date_string text,weight text)"

        db!!.execSQL(createUserSql)
        db.execSQL(createWeightSql)
        Log.e("MENG","create database")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) = Unit

}