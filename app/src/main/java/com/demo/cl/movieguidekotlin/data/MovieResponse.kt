package com.demo.cl.movieguidekotlin.data

import androidx.room.*
import com.google.gson.annotations.Expose

data class MovieResponse(
    val page: Int?,
    val results: List<Movie>?,
    val total_pages: Int?,
    val total_results: Int?
)

@Entity(tableName = "Movie", indices = [Index("category"),Index("favorite"), Index("indexInResponse")])
data class Movie constructor(
    val adult: Boolean?,
    val backdrop_path: String?,
    val id: Int,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?,
    @Expose(serialize = false, deserialize = false)
    var category:String?,
    @Expose(serialize = false, deserialize = false)
    val favorite:Boolean=false
){
    @Ignore
    var genre_ids: List<Int?>?=null
    @Expose(serialize = false, deserialize = false)
    var indexInResponse: Int = -1

//    @Expose(serialize = false, deserialize = false)
//    var timeStamp:Long?=null

    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false, deserialize = false)
    var primaryId=0
}


