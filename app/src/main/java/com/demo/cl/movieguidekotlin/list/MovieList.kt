package com.demo.cl.movieguidekotlin.list

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.demo.cl.movieguidekotlin.api.API_KEY
import com.demo.cl.movieguidekotlin.api.Category
import com.demo.cl.movieguidekotlin.api.MovieApi
import com.demo.cl.movieguidekotlin.data.Movie
import com.demo.cl.movieguidekotlin.data.MovieDao
import com.demo.cl.movieguidekotlin.list.PagingRequestHelper.Request
import com.demo.cl.movieguidekotlin.list.PagingRequestHelper.RequestType
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class MovieListDataSource(val movieApi: MovieApi, val category: Category) :
    PageKeyedDataSource<Int, Movie>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        val page = 1
        movieApi.getMovieList(category.value, API_KEY, page).subscribeOn(Schedulers.io())
            .subscribeBy(onError = {

            }, onNext = { response ->
                response.results?.let {
                    callback.onResult(it, null, page + 1)
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        movieApi.getMovieList(category.value, API_KEY, params.key).subscribeOn(Schedulers.io())
            .subscribeBy(onNext = { response ->
                response.results?.let {
                    callback.onResult(it, params.key + 1)
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

}

class MovieListDataSourceFactory(val movieApi: MovieApi, val category: Category) :
    DataSource.Factory<Int, Movie>() {
    override fun create(): DataSource<Int, Movie> {
        return MovieListDataSource(movieApi, category)
    }
}


class MovieSourceBoundaryCallback(
    val movieApi: MovieApi,
    val movieDao: MovieDao,
    val category: Category
) : PagedList.BoundaryCallback<Movie>() {
    private var currentPage: Int = 1

    private val helper = PagingRequestHelper(Executors.newSingleThreadExecutor())

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(RequestType.INITIAL, insertCurrentPageIntoDB().apply {
            currentPage = 1
            Log.e("onZeroItemsLoaded", "currentPage=$currentPage")
        })
    }

    private fun insertCurrentPageIntoDB(): (Request.Callback) -> Unit {
        return { callback ->
            Log.e("insertCurrentPageIntoDB", "start fetch from api, currentpage=${currentPage}")
            movieApi.getMovieList(category.value, API_KEY, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { response ->
                    val nextIndex = movieDao.getNextIndex(category.value)
                    Log.e(
                        "insertCurrentPageIntoDB",
                        "get response from api start map, nextIndex=${nextIndex},chunkId=${response.results!![0].id}"
                    )
                   return@map response.results?.mapIndexed { index, movie ->
                        movie.category = category.value
                        movie.indexInResponse = nextIndex + index
                        movie
                    }.apply {
                        Log.e(
                            "insertCurrentPageIntoDB",
                            "map complete, startIndex=${nextIndex},chunkId=${response.results!![0].id}"
                        )
                    }
                }.observeOn(Schedulers.io())
                .subscribe(Consumer {
                    it?.let {
                        movieDao.insertMovies(it)
                        Log.e(
                            "insertCurrentPageIntoDB",
                            "movieDao.insertMovies(it),chunkId=${it[0].id}"
                        )

                    }
                    callback.recordSuccess()
                }, Consumer {
                    Log.e("insertCurrentPageIntoDB", "callback.recordFailure(it)")
                    callback.recordFailure(it)
                })
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        helper.runIfNotRunning(RequestType.AFTER, insertCurrentPageIntoDB().apply {
            currentPage += 1
            Log.e("onItemAtEndLoaded", "currentPage=$currentPage")

        })
    }

}



