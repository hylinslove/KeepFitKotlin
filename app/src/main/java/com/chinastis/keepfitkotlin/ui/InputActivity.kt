package com.chinastis.keepfitkotlin.ui

import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.chinastis.keepfitkotlin.R
import com.chinastis.keepfitkotlin.base.BaseActivity
import com.chinastis.keepfitkotlin.widget.NumberSelector

class InputActivity : BaseActivity() {

    private lateinit var numberSelector : NumberSelector
    private lateinit var numberText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContentView(R.layout.activity_input)

        initView()
        initEvent()

    }

    private fun initView() {

        numberSelector = findViewById(R.id.number_selector_input) as NumberSelector
        numberText = findViewById(R.id.number_input) as TextView
        numberText.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        numberText.paint.isAntiAlias = true
    }

    private fun initEvent() {

        numberSelector.setOnNumberChangeListener(object : NumberSelector.OnNumberChangeListener{
            override fun onNumberChanged(number: Float) {
                numberText.text = number.toString()
            }
        } )
        numberSelector.setOriginalNumber(50f)
    }
}
