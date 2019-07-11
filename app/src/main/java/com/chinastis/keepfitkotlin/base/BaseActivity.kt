package com.chinastis.keepfitkotlin.base

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*

import com.chinastis.keepfitkotlin.R

open class BaseActivity : AppCompatActivity() {

    private var fromIntent: Intent? = null
    private var titleLayout: RelativeLayout? = null
    private var contentLayout: FrameLayout? = null
    private var titleText: TextView? = null
    private var title:String? = null
    private var icon_end: ImageView? = null
    private var textEnd: TextView? = null
    private var isHaveToolBar :Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        fromIntent = this.intent
        titleLayout = findViewById(R.id.layout_top) as RelativeLayout?
        contentLayout = findViewById(R.id.content_base) as FrameLayout?

        initToolBar()

    }

    fun setIsHaveToolBar(isHaveToolBar:Boolean) {
        if (!isHaveToolBar) {
            titleLayout!!.visibility = View.GONE
        }
    }

    fun addContentView(layoutId: Int) {
        val localView = LayoutInflater.from(this).inflate(layoutId,null)
        this.contentLayout!!.removeAllViews()
        this.contentLayout!!.addView(localView, LinearLayout.LayoutParams(-1, -1))
    }

    private fun initToolBar() {
        titleText = findViewById(R.id.title_base) as TextView?
        title = fromIntent?.getStringExtra(Constant.TITLE)
        titleText?.text = title
    }


}

