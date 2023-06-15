package com.greensun.uisamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.greensun.uisamples.list.CommonDecorationFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = CommonDecorationFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commitNowAllowingStateLoss()
    }
}