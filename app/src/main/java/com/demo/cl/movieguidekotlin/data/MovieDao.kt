package com.demo.cl.movieguidekotlin.data

import androidx.paging.DataSource
import androidx.room.*
import com.demo.cl.movieguidekotlin.api.Category


@Dao
interface MovieDao {
    @Query("select * from Movie where category = :category order by indexInResponse asc")
    fun getMovieList(category: String): DataSource.Factory<Int,Movie>

    @Insert
    fun insertMovies(movies:List<Movie>)

    @Query("SELECT MAX(indexInResponse) + 1 FROM Movie WHERE category = :category")
    fun getNextIndex(category: String) : Int

}