package com.chinastis.keepfitkotlin.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log

/**
 * Created by xianglong on 2019/7/13.
 */
class DbManager private constructor(context: Context) {

    private var database: SQLiteDatabase
    private var dbHelper: DbHelper = DbHelper(context, DbHelper.dbName, null, DbHelper.dbVersion)
    private var context: Context


    init {
        database = dbHelper.writableDatabase
        this.context = context

    }

    companion object {

        private var instance: DbManager? = null

        fun getInstance(context: Context): DbManager {

            @Synchronized
            if (instance == null) {

                if (instance == null) {
                    instance = DbManager(context)
                }
            }
            return instance!!
        }
    }

    /**
     * insert
     */
    fun inert(table: String, cv: ContentValues) {

        database = dbHelper.writableDatabase
        val index = database.insert(table, null, cv)
        Log.e("MENG","insert $index")
        database.close()
    }

    /**
     * update
     */
    fun update(table: String, cv: ContentValues, whereClause: String,
               whereArgs: Array<String?>) {

        database = dbHelper.writableDatabase
        database.update(table, cv, whereClause, whereArgs)
        database.close()
    }

    /**
     * delete
     */
    fun delete(table: String, whereClause: String,
               whereArgs: Array<String>) {

        database = dbHelper.writableDatabase
        database.delete(table, whereClause, whereArgs)
        database.close()
    }


    /**
     * query
     */
    fun select(sql: String):
            ArrayList<HashMap<String, String>> {

        database = dbHelper.writableDatabase

        //数据集合
        val list = ArrayList<HashMap<String, String>>()

        val cursor = database.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val map = HashMap<String, String>()

            for (i in cursor.columnNames.indices) {
                map.put(cursor.getColumnName(i),cursor.getString(i))
            }
            list.add(map)
        }

        cursor.close()
        database.close()
        return list
    }


}