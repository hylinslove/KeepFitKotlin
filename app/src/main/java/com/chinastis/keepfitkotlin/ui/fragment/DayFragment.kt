package com.chinastis.keepfitkotlin.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi

import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.chinastis.keepfitkotlin.R
import com.chinastis.keepfitkotlin.db.DbManager
import com.chinastis.keepfitkotlin.ui.InputActivity
import com.chinastis.keepfitkotlin.util.DateUtil
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

/**
 * Created by xianglong on 2019/7/11.
 */

class DayFragment : Fragment() {

    private lateinit var actContext: Context
    private lateinit var contentView: View
    private lateinit var type: String
    private lateinit var dbManager: DbManager

    private lateinit var weight: TextView
    private lateinit var date: TextView
    private lateinit var tip: TextView
    private lateinit var bmiText: TextView
    private lateinit var progress: ProgressBar

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.actContext = context!!
        dbManager = DbManager.getInstance(context)
    }

    companion object {
        /**
         * get Fragment
         */
        fun getChartFragment(type: String): DayFragment {
            val chartFragment = DayFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            chartFragment.arguments = bundle

            return chartFragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater?.inflate(R.layout.fragment_chart, null)!!
        type = arguments.getString("type")

        when (type) {
            "日" -> {
                initDayView()
                initDayData()
            }
        }

        return contentView

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        when (type) {
            "日" -> initDayData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initDayData() {

        val dateString = DateUtil.getDateString(Date())
        val list = dbManager.select("select * from weight where date_string = '$dateString' ")
        if (list.size > 0) {
            weight.text = list[0]["weight"]
        }
        date.text = dateString

        val listUser = dbManager.select("select * from user")
        if (listUser.size > 0) {
            try {
                val numberFormat = DecimalFormat("0.0")
                val height = listUser[0]["height"].toString().toFloat()
                val goal = listUser[0]["goal_weight"].toString().toFloat()
                val weightNow = weight.text.toString().toFloat()
                progress.setProgress(0,true)
                var progressNum: Int
                when {
                    weightNow > goal -> {
                        progressNum = (goal/weightNow*100).toInt()
                        progress.postDelayed({ progress.setProgress(progressNum,true) },500)
                        tip.text = "还需减重 ${calculate(weightNow, goal)} 公斤"
                    }
                    weightNow < goal -> {
                        progressNum = (weightNow/goal*100).toInt()
                        progress.postDelayed({ progress.setProgress(progressNum,true) },500)
                        tip.text = "还需增重 ${calculate(goal, weightNow)} 公斤"
                    }
                    else -> {
                        progress.postDelayed({ progress.setProgress(100,true) },500)
                        tip.text = "恭喜你，已经达到目标体重"
                    }
                }
                //计算bmi
                val bmi:Float = weightNow/(height*height/10000)
                val formatBmi = numberFormat.format(bmi)
                when {
                    bmi <= 18.4  -> {
                        bmiText.text = "BMI $formatBmi | 偏瘦"
                    }
                    bmi > 18.4 && bmi<24  -> {
                        bmiText.text = "BMI $formatBmi | 标准"
                    }
                    bmi >=24 && bmi < 28  -> {
                        bmiText.text = "BMI $formatBmi | 偏胖"
                    }
                    bmi >= 28  -> {
                        bmiText.text = "BMI $formatBmi | 肥胖"
                    }
                }

            } catch (e: Exception) {
                Log.e("MENG","e："+e)
            }


        }
    }


    private fun initDayView() {

        weight = contentView.findViewById(R.id.weight_day) as TextView
        date = contentView.findViewById(R.id.date_day) as TextView
        tip = contentView.findViewById(R.id.tip_day) as TextView
        bmiText = contentView.findViewById(R.id.bmi_day) as TextView
        progress = contentView.findViewById(R.id.progress_day) as ProgressBar

        val input: ImageView = contentView.findViewById(R.id.input_day) as ImageView
        input.setOnClickListener {
            context.startActivity(Intent(context, InputActivity().javaClass))
        }
    }

    private fun initMonthView() = Unit


    /**
     * float 相减
     */
    private fun calculate(f1: Float, f2:Float) : Float {

        val bd1 = BigDecimal(f1.toString())
        val bd2 = BigDecimal(f2.toString())
        return bd1.subtract(bd2).toFloat()
    }
}
