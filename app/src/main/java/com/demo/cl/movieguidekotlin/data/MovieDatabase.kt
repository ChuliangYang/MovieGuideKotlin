package com.demo.cl.movieguidekotlin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1 ,exportSchema = false)
abstract class MovieDatabase:RoomDatabase(){
    abstract fun movieDao():MovieDao

    companion object{
        @Volatile var INSTACE:MovieDatabase?=null
        fun getInstance(context:Context):MovieDatabase{
            if(INSTACE==null){
                synchronized(this){
                    if(INSTACE==null){
                        INSTACE=Room.databaseBuilder(context,MovieDatabase::class.java,"MovieDatabase").build()
                    }
                }
            }
            return  INSTACE!!
        }
    }
}