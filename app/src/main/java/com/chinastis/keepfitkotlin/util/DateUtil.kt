package com.chinastis.keepfitkotlin.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xianglong on 2019/7/16.
 */
object DateUtil {

    fun getDateString(date: Date) : String {

        val format = SimpleDateFormat("yyyy年MM月dd日")
        return format.format(date)
    }

    fun getDateTime(dateString: String) : Long {

        val format = SimpleDateFormat("yyyy年MM月dd日")

        val date = format.parse(dateString)

        return date.time
    }

}