package com.demo.cl.movieguidekotlin.Inject

import android.content.Context
import com.demo.cl.movieguidekotlin.api.MovieApi
import com.demo.cl.movieguidekotlin.api.RetrofitClient
import com.demo.cl.movieguidekotlin.data.Movie
import com.demo.cl.movieguidekotlin.data.MovieDao
import com.demo.cl.movieguidekotlin.data.MovieDatabase
import com.demo.cl.movieguidekotlin.repos.MovieRepository
import com.demo.cl.movieguidekotlin.viewmodel.MovieViewModel

object InjectionUtils {
    fun injectMovieRepo(context: Context):MovieRepository{
        return MovieRepository(injectMovieApi(),injectMovieDao(context))
    }

    fun injectMovieApi():MovieApi{
        return RetrofitClient.getInstance().create(MovieApi::class.java)
    }

    fun injectMovieDao(context: Context):MovieDao{
        return MovieDatabase.getInstance(context).movieDao()
    }
}