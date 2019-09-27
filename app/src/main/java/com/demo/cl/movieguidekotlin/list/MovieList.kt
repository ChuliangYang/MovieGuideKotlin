package com.demo.cl.movieguidekotlin.list

import android.annotation.SuppressLint
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.demo.cl.movieguidekotlin.api.API_KEY
import com.demo.cl.movieguidekotlin.api.Category
import com.demo.cl.movieguidekotlin.api.MovieApi
import com.demo.cl.movieguidekotlin.data.Movie
import com.demo.cl.movieguidekotlin.data.MovieResponse
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MovieListDataSource(val movieApi: MovieApi,val category:Category):PageKeyedDataSource<Int,Movie>(){

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        val page=1
        movieApi.getMovieList(category.value,API_KEY,page).subscribeOn(Schedulers.io()).subscribeBy(onError = {

        }, onNext = { response ->
            response.results?.let {
                callback.onResult(it,null,page+1)
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        movieApi.getMovieList(category.value,API_KEY,params.key).subscribeOn(Schedulers.io()).subscribeBy (onNext = {response->
            response.results?.let {
                callback.onResult(it,params.key+1)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

}

class MovieListDataSourceFactory(val movieApi: MovieApi,val category:Category):DataSource.Factory<Int,Movie>(){
    override fun create(): DataSource<Int, Movie> {
        return MovieListDataSource(movieApi,category)
    }
}



