package com.demo.cl.movieguidekotlin.Inject

import com.demo.cl.movieguidekotlin.api.MovieApi
import com.demo.cl.movieguidekotlin.api.RetrofitClient
import com.demo.cl.movieguidekotlin.repos.MovieRepository
import com.demo.cl.movieguidekotlin.viewmodel.MovieViewModel

object InjectionUtils {
    fun injectMovieRepo():MovieRepository{
        return MovieRepository(injectMovieApi())
    }

    fun injectMovieApi():MovieApi{
        return RetrofitClient.getInstance().create(MovieApi::class.java)
    }
}