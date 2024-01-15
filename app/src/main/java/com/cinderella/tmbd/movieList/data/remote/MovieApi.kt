package com.cinderella.tmbd.movieList.data.remote

import com.cinderella.tmbd.movieList.data.remote.respond.MovieDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/(category)")
    suspend fun getMovies(
        @Path("category") category: String,
        @Query("page") page: Int,
    ): List<MovieDto>

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }
}