package com.demo.cl.movieguidekotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.cl.movieguidekotlin.Inject.InjectionUtils
import com.demo.cl.movieguidekotlin.viewmodel.MovieViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory:ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        with(modelClass){
         return  when{
                isAssignableFrom(MovieViewModel::class.java)->
                     MovieViewModel(InjectionUtils.injectMovieRepo())
                else ->throw IllegalArgumentException()
            }as T
        }


    }

    companion object{
        @Volatile private var INSTANCE:ViewModelFactory?=null

        fun getInstance():ViewModelFactory{
            if(INSTANCE==null){
                synchronized(this){
                    if(INSTANCE==null){
                        INSTANCE=ViewModelFactory()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}

