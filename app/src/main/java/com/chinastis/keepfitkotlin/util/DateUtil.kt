package com.chinastis.keepfitkotlin.util

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by xianglong on 2019/7/16.
 */
object DateUtil {

    /**
     * 获取中文时间
     */
    fun getDateString(date: Date): String {

        val format = SimpleDateFormat("yyyy年MM月dd日")
        return format.format(date)
    }

    /**
     * 获取中文时间对应的秒数
     */
    fun getDateTime(dateString: String): Long {

        val format = SimpleDateFormat("yyyy年MM月dd日")
        val date = format.parse(dateString)

        return date.time
    }


    /**
     * 获取中文时间和天数的差值，计算对应的秒数
     */
    fun getDateTimeByOffset(dateString: String, offset: Int): Long {

        val format = SimpleDateFormat("yyyy年MM月dd日")
        val date = format.parse(dateString)

        return date.time - (offset * 86_400_000)
    }

    /**
     * 根据时间判断是周几
     */
    fun getWeekByTime(time: Long): String {

        val format = SimpleDateFormat("EEEE", Locale.CHINA)
        return format.format(Date(time))
    }

    /**
     * 获取当月的每日的time millis
     */
    fun getDaysInMonth(month: Int, year: Int): Int {

        var days = 0

        when (month) {
            1, 3, 5, 7, 8, 10, 12 -> {
                days = 31
            }
            4, 6, 9, 11 -> {
                days = 30
            }
            2 -> {
                days = if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    29
                } else {
                    28
                }
            }
        }

        return days

    }

}