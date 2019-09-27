package com.demo.cl.movieguidekotlin.repos

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.demo.cl.movieguidekotlin.api.Category
import com.demo.cl.movieguidekotlin.api.MovieApi
import com.demo.cl.movieguidekotlin.data.Movie
import com.demo.cl.movieguidekotlin.list.MovieListDataSourceFactory

class MovieRepository(val movieApi: MovieApi) {
    fun getMoviePageList(category: Category):LiveData<PagedList<Movie>>{
        return LivePagedListBuilder(MovieListDataSourceFactory(movieApi,category),10).build()
    }
}