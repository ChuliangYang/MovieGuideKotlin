package com.demo.cl.movieguidekotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demo.cl.movieguidekotlin.R
import com.demo.cl.movieguidekotlin.data.MovieDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Completable.fromAction {
            deleteDatabase("MovieDatabase")
            MovieDatabase.getInstance(baseContext).clearAllTables()
        }.subscribeOn(Schedulers.io()).subscribe {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_content,MovieFragment.newInstance())
                commit()
            }
        }




    }
}
