package com.chinastis.keepfitkotlin.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by xianglong on 2019/7/12.
 */
class DbHelper(context: Context, name:String, factory:SQLiteDatabase.CursorFactory, version:Int) :
        SQLiteOpenHelper(context,name,factory,version) {

    companion object {
        val dbName = "keep.db"
        val dbVersion = 1
    }



    override fun onCreate(db: SQLiteDatabase?) {
        val createUserSql = "create table if not exists weight(user_id text primary key," +
                "user_name text,goal_weight text)"

       val createWeightSql = "create table if not exists weight(user_id text primary key," +
               "date_sql date,date_string text)"

        db!!.execSQL(createUserSql)
        db.execSQL(createWeightSql)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) = Unit

}