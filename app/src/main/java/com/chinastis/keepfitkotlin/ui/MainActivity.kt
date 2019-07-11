package com.chinastis.keepfitkotlin.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.chinastis.keepfitkotlin.R
import com.chinastis.keepfitkotlin.adapter.ChartPagerAdapter
import com.chinastis.keepfitkotlin.ui.fragment.ChartFragment
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var fragments: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTabLayout()
        initFragment()
        initViewPager()

    }

    private fun initViewPager() {
        viewPager = findViewById(R.id.view_pager_main) as ViewPager

        val titles = arrayOf("日","周","月","年")
        val adapter = ChartPagerAdapter(supportFragmentManager,fragments)
        adapter.setTitles(titles)

        viewPager.offscreenPageLimit = titles.size - 1
        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)
    }

    private fun initFragment() {

        fragments = ArrayList()
        val dayChartFragment = ChartFragment.getChartFragment("日")
        val weekChartFragment = ChartFragment.getChartFragment("周")
        val monthChartFragment = ChartFragment.getChartFragment("月")
        val yearChartFragment = ChartFragment.getChartFragment("年")

        fragments.add(dayChartFragment)
        fragments.add(weekChartFragment)
        fragments.add(monthChartFragment)
        fragments.add(yearChartFragment)

    }

    private fun initTabLayout() {

        tabLayout = findViewById(R.id.tab_main) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("日"))
        tabLayout.addTab(tabLayout.newTab().setText("周"))
        tabLayout.addTab(tabLayout.newTab().setText("月"))
        tabLayout.addTab(tabLayout.newTab().setText("年"))


    }
}
