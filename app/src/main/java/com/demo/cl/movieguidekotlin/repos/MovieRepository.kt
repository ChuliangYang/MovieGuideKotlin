package com.demo.cl.movieguidekotlin.repos

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.demo.cl.movieguidekotlin.api.Category
import com.demo.cl.movieguidekotlin.api.MovieApi
import com.demo.cl.movieguidekotlin.data.Movie
import com.demo.cl.movieguidekotlin.data.MovieDao
import com.demo.cl.movieguidekotlin.list.MovieListDataSourceFactory
import com.demo.cl.movieguidekotlin.list.MovieSourceBoundaryCallback

class MovieRepository(val movieApi: MovieApi, val movieDao: MovieDao) {
    fun getMoviePageList(category: Category):LiveData<PagedList<Movie>>{
        return LivePagedListBuilder(movieDao.getMovieList(category.value),PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(20).build()).setBoundaryCallback(
            MovieSourceBoundaryCallback(movieApi,movieDao,category)
        ).build()
    }
}