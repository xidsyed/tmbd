package com.cinderella.tmbd.movieList.data.Mapper

import com.cinderella.tmbd.movieList.data.local.MovieEntity
import com.cinderella.tmbd.movieList.data.remote.respond.MovieDto
import com.cinderella.tmbd.movieList.domain.model.Movie


fun MovieDto.toMovieEntity(category: String): MovieEntity {
    return MovieEntity(
        id = id ?: -1,
        title = title ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        backdrop_path = backdrop_path ?: "",
        vote_average = vote_average ?: 0.0,
        release_date = release_date ?: "",
        category = category,
        adult = adult ?: false,
        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        original_language = original_language ?: "",
        original_title = original_title ?: "",
        popularity = popularity ?: 0.0,
        video = video ?: false,
        vote_count = vote_count ?: 0
    )

}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id ?: -1,
        title = title ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        backdrop_path = backdrop_path ?: "",
        vote_average = vote_average ?: 0.0,
        release_date = release_date ?: "",
        category = category,
        adult = adult ?: false,
        genre_ids = try {
            genre_ids?.split(",")?.map{it.toInt()} ?: listOf(-1,-2)
        } catch (e: Exception) {
            listOf(-1,-2)
        },
        original_language = original_language ?: "",
        original_title = original_title ?: "",
        popularity = popularity ?: 0.0,
        video = video ?: false,
        vote_count = vote_count ?: 0
    )
}