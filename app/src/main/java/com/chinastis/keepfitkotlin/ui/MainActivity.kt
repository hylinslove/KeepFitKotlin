package com.chinastis.keepfitkotlin.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import com.chinastis.keepfitkotlin.R
import com.chinastis.keepfitkotlin.adapter.ChartPagerAdapter
import com.chinastis.keepfitkotlin.ui.fragment.ChartFragment
import java.util.ArrayList
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var titles:Array<String>
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var fragments: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        titles = arrayOf("日","周","月","年")

        initTabLayout()
        initFragment()
        initViewPager()

    }

    private fun initViewPager() {

        val adapter = ChartPagerAdapter(supportFragmentManager,fragments)
        adapter.setTitles(titles)

        viewPager = findViewById(R.id.view_pager_main) as ViewPager
        viewPager.offscreenPageLimit = titles.size - 1
        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)

    }

    private fun initFragment() {

        fragments = ArrayList()

        titles.map { ChartFragment.getChartFragment(it) }
                .forEach { fragments.add(it) }

    }

    private fun initTabLayout() {
        tabLayout = findViewById(R.id.tab_main) as TabLayout

        titles.forEach { title -> tabLayout.addTab(tabLayout.newTab().setText(title)) }

    }
}
