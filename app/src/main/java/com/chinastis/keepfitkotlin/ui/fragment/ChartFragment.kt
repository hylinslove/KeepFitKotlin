package com.chinastis.keepfitkotlin.ui.fragment

import android.content.Context
import android.os.Bundle

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chinastis.keepfitkotlin.R

/**
 * Created by xianglong on 2019/7/11.
 */

class ChartFragment : Fragment() {

    private lateinit var actContext: Context
    private lateinit var contentView: View
    private lateinit var type: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.actContext = context!!
    }

    companion object {
        /**
         * get Fragment
         */
        fun getChartFragment(type: String):ChartFragment {
            val chartFragment = ChartFragment()
            val bundle = Bundle()
            bundle.putString("type",type)
            chartFragment.arguments = bundle

            return chartFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater?.inflate(R.layout.fragment_chart,null)!!
        type = arguments.getString("type")


        return contentView

    }


}
