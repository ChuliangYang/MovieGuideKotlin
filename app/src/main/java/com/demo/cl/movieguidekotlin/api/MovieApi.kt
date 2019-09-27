package com.demo.cl.movieguidekotlin.api

import com.demo.cl.movieguidekotlin.data.MovieResponse
import com.google.gson.Gson
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("{category}")
    fun getMovieList(@Path("category") category:String, @Query("api_key") apiKey:String, @Query("page") page:Int,@Query("language") language:String="en-US"):Observable<MovieResponse>
}


object RetrofitClient{
    @Volatile private var INSTANCE: Retrofit?=null
    fun getInstance():Retrofit{
        if(INSTANCE==null){
            synchronized(this){
                if(INSTANCE==null){
                    INSTANCE=Retrofit.Builder().client(OkHttpClient.Builder().let {
                                it.addInterceptor(HttpLoggingInterceptor().apply {
                                    level= HttpLoggingInterceptor.Level.BASIC
                                })
                                it.build()
                    }).baseUrl("http://api.themoviedb.org/3/movie/").addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
                }
            }
        }
        return INSTANCE!!
    }
}

enum class Category(val value:String){
    POPULAR("popular"),
    TOP_RATED("top_rated")
}

const val API_KEY="58db6e9f579236f079b9460f081da511"


const val PICTURE_BASE_URL = "http://image.tmdb.org/t/p/w185/"