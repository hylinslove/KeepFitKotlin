package com.chinastis.keepfitkotlin.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker

import com.chinastis.keepfitkotlin.R


/**
 * Created by xianglong on 2018/12/7.
 */

class NumberPickerDialog(context: Context) : Dialog(context) {

    private lateinit var pickerNum: NumberPicker
    private lateinit var cancelNum: Button
    private lateinit var confirmNum: Button
    private var listener: OnNumberSelectedListener? = null

    private var num = 170

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_number_picker)
        iniView()
        initNumberPicker()


    }

    private fun iniView() {
        pickerNum = findViewById(R.id.picker_num) as NumberPicker
        cancelNum = findViewById(R.id.cancel_num) as Button
        confirmNum = findViewById(R.id.confirm_num) as Button

        cancelNum.setOnClickListener { dismiss() }
        confirmNum.setOnClickListener {
            if (listener != null) {
                listener!!.numberSelected(num)
            }
            dismiss()
        }
    }

    private fun initNumberPicker() {
        //设置最大值
        pickerNum.maxValue = 250
        //设置最小值
        pickerNum.minValue = 1
        //设置当前值
        pickerNum.value = 170
        //设置不可编辑
        pickerNum.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        //设置滑动监听
        pickerNum.setOnValueChangedListener { _, _, newVal -> num = newVal }
    }

    fun setNumberSelectedListener(listener: OnNumberSelectedListener?) {
        this.listener = listener
    }

    interface OnNumberSelectedListener {
        fun numberSelected(number: Int)
    }

}
