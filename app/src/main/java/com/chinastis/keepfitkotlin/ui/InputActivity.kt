package com.chinastis.keepfitkotlin.ui

import android.app.DatePickerDialog
import android.content.ContentValues
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.chinastis.keepfitkotlin.R
import com.chinastis.keepfitkotlin.base.BaseActivity
import com.chinastis.keepfitkotlin.db.DbManager
import com.chinastis.keepfitkotlin.util.DateUtil
import com.chinastis.keepfitkotlin.widget.NumberSelector
import java.lang.StringBuilder
import java.util.*


class InputActivity : BaseActivity() {

    private lateinit var dbManager: DbManager
    private lateinit var numberSelector : NumberSelector
    private lateinit var numberText : TextView
    private lateinit var confirm: Button
    private lateinit var dateText: TextView
    private  var userId: String? = "0000"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContentView(R.layout.activity_input)

        initUser()
        initView()
        initEvent()

    }

    private fun initUser() {
        dbManager = DbManager.getInstance(this)
        val list = dbManager.select("select * from user")
        if (list.size > 0) {
            userId = list[0]["user_id"]
        }

    }

    private fun initView() {

        confirm = findViewById(R.id.confirm_input) as Button
        dateText = findViewById(R.id.date_input) as TextView
        dateText.text = DateUtil.getDateString(Date())
        numberSelector = findViewById(R.id.number_selector_input) as NumberSelector
        numberText = findViewById(R.id.number_input) as TextView
        numberText.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        numberText.paint.isAntiAlias = true
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun initEvent() {
        numberSelector.setOnNumberChangeListener(object : NumberSelector.OnNumberChangeListener{
            override fun onNumberChanged(number: Float) {
                numberText.text = number.toString()
            }
        } )

        val listWeight = dbManager.select("select weight from weight order by date_time desc")
        if (listWeight.size > 0) {
            try {
                val weight: Float = listWeight[0]["weight"]!!.toFloat()
                numberSelector.setOriginalNumber(weight)
            }catch (e:Exception) {
                numberSelector.setOriginalNumber(50f)
            }
        } else{
            numberSelector.setOriginalNumber(50f)
        }


        confirm.setOnClickListener {
            val dateString = dateText.text.toString()
            val cv = ContentValues()
            cv.put("user_id",userId)
            cv.put("weight",numberText.text.toString())
            cv.put("date_string",dateString)
            cv.put("date_time",DateUtil.getDateTime(dateString))
            Log.e("MENG","time millis:"+dateString)

            val list = dbManager.select("select * from weight where date_string = '$dateString'")

            if (list.size > 0) {

                dbManager.update("weight",cv,"date_string = ?" , arrayOf(dateString))
            } else {

                dbManager.inert("weight",cv)
            }

            Toast.makeText(this,"输入成功！", Toast.LENGTH_SHORT).show()
            this.finish()
        }


        val dateDialog =  DatePickerDialog(this)
        dateDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            Log.e("MENG","year:"+year)
            Log.e("MENG","month:"+month)
            Log.e("MENG","dayOfMonth:"+dayOfMonth)

            val stringBuilder = StringBuilder()

            if (month<10) {

            }

            dateText.text =  stringBuilder.append(year).append("年")
                    .append(month+1).append("月")
                    .append(dayOfMonth).append("日")

        }

        dateText.setOnClickListener({
            dateDialog.show()
        })


    }
}
