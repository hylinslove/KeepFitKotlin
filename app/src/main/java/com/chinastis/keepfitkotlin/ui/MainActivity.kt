package com.chinastis.keepfitkotlin.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.chinastis.keepfitkotlin.R
import com.chinastis.keepfitkotlin.adapter.ChartPagerAdapter
import com.chinastis.keepfitkotlin.base.Constant
import com.chinastis.keepfitkotlin.db.DbManager
import com.chinastis.keepfitkotlin.ui.fragment.DayFragment
import com.chinastis.keepfitkotlin.ui.fragment.WeekFragment
import com.google.android.material.tabs.TabLayout
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private val context: Context = this
    private lateinit var titles:Array<String>
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var fragments: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        titles = arrayOf("日","周","月","年")

        initSetting()
        initTabLayout()
        initFragment()
        initViewPager()

        val dbManager = DbManager.getInstance(this)


    }

    private fun initSetting() {
        val setting: ImageView  = findViewById(R.id.setting_main) as ImageView
        setting.setOnClickListener({
            val intent = Intent(context,SettingActivity().javaClass)
            intent.putExtra(Constant.TITLE,"设置")
            startActivity(intent)
        })
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

        for (i in titles.indices) {

            when (titles[i]) {
                "日" -> {
                    val dayFragment: Fragment = DayFragment.getChartFragment(titles[i])
                    fragments.add(dayFragment)
                }
                "周" -> {
                    val weekFragment: Fragment = WeekFragment.getChartFragment(titles[i])
                    fragments.add(weekFragment)
                }
                "月" -> {
                    val monthFragment: Fragment = WeekFragment.getChartFragment(titles[i])
                    fragments.add(monthFragment)
                }
                "年" -> {
                    val yearFragment: Fragment = WeekFragment.getChartFragment(titles[i])
                    fragments.add(yearFragment)
                }
            }
        }


    }

    private fun initTabLayout() {
        tabLayout = findViewById(R.id.tab_main) as TabLayout

        titles.forEach { title -> tabLayout.addTab(tabLayout.newTab().setText(title)) }

    }
}
