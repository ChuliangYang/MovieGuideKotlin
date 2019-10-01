package com.demo.cl.movieguidekotlin.list

import android.util.TimeUtils
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.demo.cl.movieguidekotlin.api.API_KEY
import com.demo.cl.movieguidekotlin.api.Category
import com.demo.cl.movieguidekotlin.api.MovieApi
import com.demo.cl.movieguidekotlin.data.Movie
import com.demo.cl.movieguidekotlin.data.MovieDao
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.sql.Timestamp
import java.util.concurrent.Executors

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
        movieApi.getMovieList(category.value,API_KEY,params.key).subscribeOn(Schedulers.io()).subscribeBy (onNext = { response->
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


class MovieSourceBoundaryCallback(val movieApi: MovieApi,val movieDao:MovieDao,val category:Category):PagedList.BoundaryCallback<Movie>(){
    private var currentPage:Int=1

    private val helper=PagingRequestHelper(Executors.newSingleThreadExecutor())

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL, insertCurrentPageIntoDB(currentPage))
    }

    private fun insertCurrentPageIntoDB(page:Int): (PagingRequestHelper.Request.Callback) -> Unit {
        return { callback ->
            movieApi.getMovieList(category.value, API_KEY, page).subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .map { response ->
                    val nextIndex=movieDao.getNextIndex(category.value)
                    response.results?.mapIndexed { index, movie ->
                        movie.category = category.value
                        movie.indexInResponse = nextIndex+index
                        movie
                    }
                }.subscribe(Consumer {
                    it?.let {
                        movieDao.insertMovies(it)
                        callback.recordSuccess()
                    }
                }, Consumer {
                    callback.recordFailure(it)
                })
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER,insertCurrentPageIntoDB(++currentPage))
    }

}



