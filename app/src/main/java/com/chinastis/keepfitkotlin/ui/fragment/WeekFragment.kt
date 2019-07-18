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
import com.chinastis.keepfitkotlin.db.DbManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter

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
        val dataList = dbManager.select("select * from weight order by date_time desc")
        val entryList = ArrayList<Entry>()

        //源数据
        for (i in dataList.indices) {

            val weight: Float = dataList[i]["weight"]!!.toFloat()
            entryList.add(Entry(i.toFloat(), weight))
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
        //设置x轴
        val x = lineChart.xAxis
        //x轴位置
        x.position = XAxis.XAxisPosition.BOTTOM
        //坐标的间隔
        x.granularity = 1f
        //标签的数量
        x.labelCount = 7
        //x轴最大值最小值
        x.axisMaximum = 6f
        x.axisMinimum = 0f
        //网格线
        x.setDrawAxisLine(false)
        //标签倾斜
        x.labelRotationAngle = 45f
        //设置字符串标签
        x.setValueFormatter { value, _ ->
            var index = value.toInt()
            Log.e("MENG","value:"+value)
            "周${++index}"
        }

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


    }
}