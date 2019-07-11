package com.chinastis.keepfitkotlin.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

import com.chinastis.keepfitkotlin.R
import com.chinastis.keepfitkotlin.base.BaseActivity


class SplashActivity : BaseActivity() {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContentView(R.layout.activity_splash)

        setIsHaveToolBar(false)

        handler.postDelayed({
            startActivity(Intent(this, MainActivity().javaClass) )
            this.finish()
        },1000)



    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()

    }
}
