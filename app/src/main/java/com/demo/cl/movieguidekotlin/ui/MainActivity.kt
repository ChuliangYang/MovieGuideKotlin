package com.demo.cl.movieguidekotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.cl.movieguidekotlin.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_content,MovieFragment.newInstance())
            commit()
        }

    }
}
