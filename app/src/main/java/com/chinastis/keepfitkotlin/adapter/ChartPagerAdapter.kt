package com.chinastis.keepfitkotlin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 * Created by xianglong on 2019/7/11.
 */

class ChartPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>?) :
        FragmentPagerAdapter(fm) {
    private var titles: Array<String>? = null

    /**
     * 设置tab的标题
     * @param titles 标题
     */
    fun setTitles(titles: Array<String>) {
        this.titles = titles
    }

    override fun getItem(position: Int): Fragment = fragments!![position]

    override fun getCount(): Int = fragments?.size ?: 0

    override fun getPageTitle(position: Int): CharSequence = titles!![position]
}

