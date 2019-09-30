package com.demo.cl.movieguidekotlin.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.demo.cl.movieguidekotlin.api.Category
import com.demo.cl.movieguidekotlin.data.Movie
import com.demo.cl.movieguidekotlin.data.MovieResponse
import com.demo.cl.movieguidekotlin.repos.MovieRepository

class MovieViewModel(val repo:MovieRepository):ViewModel() {
    val moviePageList:MediatorLiveData<PagedList<Movie>> = MediatorLiveData()

    val showProgress:MutableLiveData<Int> = MutableLiveData(View.VISIBLE)

    val currentCategory=MutableLiveData<Category>(Category.POPULAR)

    private var currentMoviePage:LiveData<PagedList<Movie>>?=null

    fun getMovieList(category: Category){
        if(currentMoviePage!=null){
            moviePageList.removeSource(currentMoviePage!!)
        }
        currentMoviePage=repo.getMoviePageList(category)
        moviePageList.addSource(currentMoviePage!!) {
            moviePageList.postValue(it)
            currentCategory.postValue(category)
        }
    }



    override fun onCleared() {
        super.onCleared()
    }
}