package com.chinastis.keepfitkotlin.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.chinastis.keepfitkotlin.R

import java.util.ArrayList


class NumberSelector : LinearLayout {

    private lateinit var mContext: Context
    private var paint: Paint? = null

    private var width: Float = 0.toFloat()
    private var height: Float = 0.toFloat()
    private  var strokeWidth: Int = 0

    private  var maxWeight: Int = 0
    private  var minWeight: Int = 0
    private  var range: Float = 0f

    private var degreeRV: RecyclerView? = null
    private var numberRV: RecyclerView? = null

    private val numberList = ArrayList<Int>()
    private val degreeList = ArrayList<Int>()

    private val NUMBER_TYPE = 0
    private val DEGREE_TYPE = 1

    private var layoutManagerDegree: LinearLayoutManager? = null
    private var layoutManagerNumber: LinearLayoutManager? = null
    private var selectedPosition = -1

    private var selectedPositionNum = -1

    //    private SparseArray<ImageView> imageViews = new SparseArray<>();
    private val numberTexts = SparseArray<TextView>()


    private var originalNumber: Float = 0.toFloat()
    private var onNumberChangeListener: OnNumberChangeListener? = null
    private lateinit var bitmap: Bitmap

    /**
     * 设置数值变化监听器
     * @param listener 监听器
     */
    fun setOnNumberChangeListener(listener: OnNumberChangeListener) {
        this.onNumberChangeListener = listener
    }

    /**
     * 设置初始值
     * @param originalNumber 初始值
     */
    fun setOriginalNumber(originalNumber: Float) {
        this.originalNumber = originalNumber
        val position = (originalNumber * 10).toInt()
    }

    constructor(context: Context) : super(context) 

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.mContext = context
        LayoutInflater.from(context).inflate(R.layout.number_selector_layout, this)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_index)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.NumberSelector)
        maxWeight = ta.getInteger(R.styleable.NumberSelector_max_number, 100)
        minWeight = ta.getInteger(R.styleable.NumberSelector_min_number, 0)
        strokeWidth = ta.getInteger(R.styleable.NumberSelector_stroke_width, 4)
        range = ta.getFloat(R.styleable.NumberSelector_range, 0.1f)
        ta.recycle()
        initData()
        intView()
        intiPaint()
        setWillNotDraw(false)

        this.postDelayed({
            val position = (originalNumber * 10).toInt()
            val first = layoutManagerDegree!!.findFirstVisibleItemPosition()
            val last = layoutManagerDegree!!.findLastVisibleItemPosition()
            degreeRV!!.smoothScrollToPosition(position + (last - first) / 2 + 1)
        }, 1000)

    }

    /**
     * 初始化画笔
     */
    private fun intiPaint() {
        this.paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.color = Color.LTGRAY
        paint!!.strokeWidth = strokeWidth.toFloat()

    }

    /**
     * 初始化数据
     */
    private fun initData() {

        for (i in 0 until maxWeight * 10) {
            degreeList.add(i)

            if (i % 10 == 0) {
                numberList.add(i / 10)
            }
        }
    }

    /**
     * 初始化视图
     */
    private fun intView() {
        degreeRV = this.findViewById(R.id.degree_rv) as RecyclerView
        numberRV = this.findViewById(R.id.number_rv) as RecyclerView

        layoutManagerDegree = LinearLayoutManager(context)
        layoutManagerDegree!!.orientation = LinearLayoutManager.HORIZONTAL
        degreeRV!!.layoutManager = layoutManagerDegree

        layoutManagerNumber = LinearLayoutManager(context)
        layoutManagerNumber!!.orientation = LinearLayoutManager.HORIZONTAL
        numberRV!!.layoutManager = layoutManagerNumber

        degreeRV!!.adapter = NumberSelectorAdapter(degreeList, context, DEGREE_TYPE)
        numberRV!!.adapter = NumberSelectorAdapter(numberList, context, NUMBER_TYPE)

        degreeRV!!.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView!!.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    numberRV!!.scrollBy(dx, dy)
                    refreshSelectNumber()
                }
            }
        })

        numberRV!!.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView!!.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    degreeRV!!.scrollBy(dx, dy)
                    refreshSelectNumber()

                }
            }
        })

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize.toFloat()
        } else {
            width = measuredWidth.toFloat()
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize.toFloat()
        } else {
            height = measuredHeight.toFloat()
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        paint!!.style = Paint.Style.STROKE
        canvas.drawArc((strokeWidth / 2).toFloat(), (strokeWidth / 2).toFloat(), height - strokeWidth / 2, height - strokeWidth / 2,
                90f, 180f, false, paint!!)
        canvas.drawArc(width - height - strokeWidth.toFloat(), (strokeWidth / 2).toFloat(), width - strokeWidth / 2, height - strokeWidth / 2,
                -90f, 180f, false, paint!!)

        paint!!.style = Paint.Style.FILL
        canvas.drawRect(height / 2, 0f, width - height / 2, strokeWidth.toFloat(), paint!!)
        canvas.drawRect(height / 2, height - strokeWidth, width - height / 2, height, paint!!)

        canvas.drawBitmap(bitmap,
                width / 2 - bitmap.width / 2,
                height - bitmap.height.toFloat() - 2f, paint)

        super.onDraw(canvas)
    }


    /**
     * 更新选中的数字
     */
    private fun refreshSelectNumber() {
        val first = layoutManagerDegree!!.findFirstVisibleItemPosition()
        val last = layoutManagerDegree!!.findLastVisibleItemPosition()
        val oldSelectedPosition = selectedPosition
        selectedPosition = (last - first) / 2 + first

        if (onNumberChangeListener != null) {
            val ori = degreeList[selectedPosition].toFloat()
            val zhengshu = ori / 10

            onNumberChangeListener!!.onNumberChanged(zhengshu)
        }

        if (selectedPosition != oldSelectedPosition) {

            if (selectedPosition % 10 == 0) {
                selectedPositionNum = selectedPosition / 10

                if (numberTexts.get(selectedPositionNum) != null) {
                    numberTexts.get(selectedPositionNum).setTextColor(Color.rgb(242, 131, 76))
                }


            } else {
                if (numberTexts.get(selectedPositionNum) != null) {
                    numberTexts.get(selectedPositionNum).setTextColor(Color.LTGRAY)
                }
            }

        }

    }


    /**
     * 数字滑动适配器
     */
    private inner class NumberSelectorAdapter internal constructor(private val data: List<Int>?, private val mContext: Context, private val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        internal var totalHolderCount = 0
        internal var totalTextCount = 0
        private  var inflater: LayoutInflater = LayoutInflater.from(this.mContext)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView: View

            if (type == DEGREE_TYPE) {
                totalHolderCount++
                itemView = inflater.inflate(R.layout.item_degree, parent, false)
            } else {
                totalTextCount++
                itemView = inflater.inflate(R.layout.item_number, parent, false)
            }

            return NumberSelectorViewHolder(itemView, type)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val viewHolder = holder as NumberSelectorViewHolder
            if (type == DEGREE_TYPE) {

            } else {
                numberTexts.put(position, viewHolder.text)
                if (numberTexts.size() > totalTextCount) {
                    numberTexts.remove(position - totalTextCount)
                    numberTexts.remove(position + totalTextCount)
                }

                if (data!![position] < 10)
                    viewHolder.text.text = "" + data[position]
                else
                    viewHolder.text.text = data[position].toString() + ""

            }
        }

        override fun getItemCount(): Int {
            return data?.size ?: 0
        }


        internal inner class NumberSelectorViewHolder(itemView: View, type: Int) : RecyclerView.ViewHolder(itemView) {

            lateinit var text: TextView
            lateinit var image: ImageView

            init {
                if (type == DEGREE_TYPE) {
                    image = itemView.findViewById(R.id.item_degree_text) as ImageView
                } else {
                    text = itemView.findViewById(R.id.item_number_text) as TextView

                }
            }
        }
    }


    /**
     * 监听器
     */
    public interface OnNumberChangeListener {
        fun onNumberChanged(number: Float)
    }

}
