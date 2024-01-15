package com.cinderella.tmbd.movieList.di

import com.cinderella.tmbd.BuildConfig
import com.cinderella.tmbd.movieList.data.remote.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(MovieApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().addInterceptor(
                    Interceptor { chain ->
                        val request = chain.request()
                        val newUrl =
                            request.url.newBuilder()
                                .addQueryParameter("api_key", BuildConfig.API_KEY)
                                .build()
                        val newRequest = request.newBuilder().url(newUrl).build()
                        return@Interceptor chain.proceed(newRequest)
                    }
                ).build()
            ).build()
    }

    @Provides
    @Singleton
    fun providesMovieApi(retrofit: Retrofit) = retrofit.create(MovieApi::class.java)
}