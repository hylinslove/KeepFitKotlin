package com.chinastis.keepfitkotlin.ui

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.chinastis.keepfitkotlin.R
import com.chinastis.keepfitkotlin.base.BaseActivity
import com.chinastis.keepfitkotlin.db.DbManager
import com.chinastis.keepfitkotlin.widget.NumberPickerDialog
import com.chinastis.keepfitkotlin.widget.NumberSelector

class SettingActivity : BaseActivity() {

    private lateinit var dbManager: DbManager
    private  var userId: String? = "0000"
    private  var heightNum: Int = 170

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContentView(R.layout.activity_setting)
        dbManager = DbManager.getInstance(this)
        initViewEvent()
    }

    private fun initViewEvent() {

        val numberSelector: NumberSelector = findViewById(R.id.number_selector_setting) as NumberSelector
        val heightText: TextView = findViewById(R.id.height_setting) as TextView
        val numberText: TextView = findViewById(R.id.goal_setting) as TextView
        val confirm: Button = findViewById(R.id.confirm_setting) as Button

        numberSelector.setOnNumberChangeListener(object : NumberSelector.OnNumberChangeListener{
            override fun onNumberChanged(number: Float) {
                numberText.text = number.toString()
            }
        })
        numberSelector.setOriginalNumber(50f)

        val numberPickerDialog = NumberPickerDialog(this)
        numberPickerDialog.setNumberSelectedListener(object : NumberPickerDialog.OnNumberSelectedListener{
            override fun numberSelected(number: Int) {
                heightText.text = number.toString() + " cm"
                heightNum = number
            }

        })


        heightText.setOnClickListener({
            numberPickerDialog.show()
        })

        confirm.setOnClickListener({
            val list = dbManager.select("select * from user")

            if (list.size > 0) {
                userId = list[0]["user_id"]
                val cv = ContentValues()
                cv.put("goal_weight",numberText.text.toString())
                cv.put("height",heightNum.toString())
                dbManager.update("user",cv,"user_id = ? ",arrayOf(userId))

            } else {
                val cv = ContentValues()
                cv.put("user_id",userId)
                cv.put("user_name","default_user")
                cv.put("goal_weight",numberText.text.toString())
                cv.put("height",heightNum.toString())
                dbManager.inert("user",cv)
            }

            Toast.makeText(this,"输入成功！", Toast.LENGTH_SHORT).show()
            this.finish()
        })


    }
}
