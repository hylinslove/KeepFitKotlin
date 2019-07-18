package com.chinastis.keepfitkotlin.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chinastis.keepfitkotlin.R
import com.chinastis.keepfitkotlin.base.Constant
import com.chinastis.keepfitkotlin.db.DbManager
import com.chinastis.keepfitkotlin.util.DateUtil
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by xianglong on 2019/7/17.
 */
class WeekFragment : Fragment() {

    private lateinit var actContext: Context
    private lateinit var contentView: View
    private lateinit var type: String
    private lateinit var dbManager: DbManager

    private lateinit var lineChart: LineChart

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.actContext = context!!
        dbManager = DbManager.getInstance(context)
    }

    companion object {
        /**
         * get Fragment
         */
        fun getChartFragment(type: String): WeekFragment {
            val chartFragment = WeekFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            chartFragment.arguments = bundle

            return chartFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater?.inflate(R.layout.fragment_week, null)!!
        type = arguments.getString("type")

        initLineChart()

        return contentView

    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {

//        val entryList = ArrayList<Entry>()

//        val dataList = dbManager.select("select * from weight order by date_time desc")
        var entryList = ArrayList<Entry>()
        //源数据
//        for (i in 0..5) {
//
//            val weight: Float = dataList[i]["weight"]!!.toFloat()
//            entryList.add(Entry(i.toFloat(), weight))
//        }
        when (type) {
            "周" -> {
                entryList = getWeekData()
            }

            "月" -> {
                entryList = getMonthData()

            }

            "年" -> {
                entryList = getYearData()

            }
        }

        if (entryList.size == 0) {
            return
        }


        //初始化折线数据集合
        val dataSet = LineDataSet(entryList,"my weight")
        //line color
        dataSet.color = context.resources.getColor(R.color.colorAccent)
        //line width
        dataSet.lineWidth = 4f
        //show dot
        dataSet.setDrawCircles(true)
        //line smooth
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        //初始化折线数据
        val lineData = LineData(dataSet)
        //显示数值
        lineData.setDrawValues(true)
        lineData.setValueTextColor(Color.WHITE)
        lineData.setValueTextSize(12f)

        lineChart.data = lineData
        lineChart.invalidate()
        //动画
        lineChart.animateY(1000)
        lineChart.animateX(1000)

    }

    private fun initLineChart() {
        lineChart  = contentView.findViewById(R.id.line_chart_week) as LineChart
        //显示边界
        lineChart.setDrawBorders(true)
        //边界颜色
        lineChart.setBorderColor(Color.DKGRAY)
        //显示网格
        lineChart.setDrawGridBackground(false)
        //网格颜色
        lineChart.setGridBackgroundColor(Color.BLACK)
        //动画
        lineChart.animateY(1500)
        lineChart.animateX(1500)
        //双击放大
        lineChart.isDoubleTapToZoomEnabled = false

        //设置x轴
        val x = lineChart.xAxis
        //x轴位置
        x.position = XAxis.XAxisPosition.BOTTOM
        //网格线
        x.setDrawAxisLine(false)
        //标签倾斜
        x.labelRotationAngle = 45f

        x.setDrawLabels(true)
        x.gridColor = Color.BLACK
        x.textColor = Color.DKGRAY

        //右边的让它不显示
        val right = lineChart.axisRight
        right.setDrawGridLines(false)
        right.setDrawAxisLine(false)
        right.gridColor = Color.BLACK


        //设置左边的坐标
        val left = lineChart.axisLeft
        left.setDrawLabels(true)
        left.textColor = Color.DKGRAY
        left.setDrawGridLines(true)
        left.setDrawAxisLine(false)
        left.enableGridDashedLine(10f, 10f, 0f)
        left.gridColor = Color.DKGRAY

        left.axisMaximum = 80f
        left.axisMinimum = 20f
        left.granularity = 10f

        if (Constant.GOAL_WEIGHT!=null
                && !Constant.GOAL_WEIGHT.equals("")
                && !Constant.GOAL_WEIGHT.equals("0")) {

            //设置限制线 12代表某个该轴某个值，也就是要画到该轴某个值上
            val limitLine = LimitLine(60f)
            //设置限制线的宽
            limitLine.lineWidth = 0.5f
            //设置限制线的颜色
            limitLine.lineColor = Color.RED
            //设置基线的位置
            limitLine.labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
            limitLine.label = "目标线"
            limitLine.textColor = Color.RED
            limitLine.textSize = 8f
            //设置限制线为虚线
            limitLine.enableDashedLine(10f, 10f, 0f)
            //左边Y轴添加限制线
            left.addLimitLine(limitLine)

        }

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() = Unit

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    Log.e("MENG","value:"+e.y)

                }
            }
        })

        lineChart.setDrawMarkers(true)
    }

    private fun getWeekData(): ArrayList<Entry> {
        val today  = DateUtil.getDateString(Date())
        val labelList = ArrayList<String>()

        val entryList = ArrayList<Entry>()
        //源数据
        for (i in 0..6) {
            val dateTime = DateUtil.getDateTimeByOffset(today,(6-i))
            val dataList = dbManager.select("select * from weight where date_time = $dateTime")

            labelList.add(DateUtil.getWeekByTime(dateTime))

            if (dataList.size == 0) {
                continue
            } else{
                val weight: Float = dataList[0]["weight"]!!.toFloat()
                entryList.add(Entry(i.toFloat(), weight))
            }
        }

        //设置x轴
        val x = lineChart.xAxis
        //坐标的间隔
        x.granularity = 1f
        //标签的数量
        x.labelCount = 7
        //x轴最大值最小值
        x.axisMaximum = 6f
        x.axisMinimum = 0f
        //设置字符串标签
        x.setValueFormatter { value, _ ->
            val index = value.toInt()
            labelList[index]
        }

        return entryList
    }

    private fun getMonthData(): ArrayList<Entry> {
        val labelList = ArrayList<String>()
        val entryList = ArrayList<Entry>()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)

        val days = DateUtil.getDaysInMonth(++month,year)

        labelList.add("")

        for (i in 1..days) {
            val dateString = "${year}年${month}月${i}日"
            val format = SimpleDateFormat("yyyy年MM月dd日")
            val date = format.parse(dateString)

            labelList.add(dateString.replace("${year}年", ""))

            val dateTime = date.time
            val dataList = dbManager.select("select * from weight where date_time = $dateTime")

            if (dataList.size == 0) {
                continue
            } else{
                val weight: Float = dataList[0]["weight"]!!.toFloat()
                entryList.add(Entry(i.toFloat(), weight))
            }

        }


        //设置x轴
        val x = lineChart.xAxis
        //坐标的间隔
        x.granularity = 5f
        //标签的数量
        x.labelCount = labelList.size+1
        //x轴最大值最小值
        x.axisMaximum = labelList.size+2f
        x.axisMinimum = 0f
        //设置字符串标签
        x.setValueFormatter { value, _ ->
            val index = value.toInt()
            labelList[index]
        }

        return entryList
    }


    private fun getYearData(): ArrayList<Entry> {
        val labelList = ArrayList<String>()
        val entryList = ArrayList<Entry>()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)

        labelList.add("")

        for (i in 1..12) {
            val dateString = "${year}年${i}月"

            labelList.add("${i}月")

            val dataList = dbManager.select("select * from weight where date_string like '$dateString%'")

            if (dataList.size == 0) {
                Log.e("MENG","${year}年 $i 月 null")
                continue
            } else{
                val count = dataList.size
                val weightTotal = dataList
                        .map { it["weight"]!!.toFloat() }
                        .sum()

                entryList.add(Entry(i.toFloat(), weightTotal/count))
            }
        }

        labelList.add("")

        //设置x轴
        val x = lineChart.xAxis
        //坐标的间隔
        x.granularity = 1f
        //标签的数量
        x.labelCount = labelList.size+1
        //x轴最大值最小值
        x.axisMaximum = labelList.size.toFloat()-1
        x.axisMinimum = 0f
        //设置字符串标签
        x.setValueFormatter { value, _ ->
            val index = value.toInt()
            labelList[index]
        }

        return entryList
    }
}