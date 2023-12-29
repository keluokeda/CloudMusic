package com.ke.music.fold.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.ke.music.api.MusicApi
import com.ke.music.fold.db.AppMemoryDatabase
import com.ke.music.fold.db.dao.CommentDao
import com.ke.music.fold.store.TokenStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object SingletonModule {

    @Provides
    @Singleton
    fun provideMusicApi(tokenStore: TokenStore): MusicApi {

        val logger = HttpLoggingInterceptor {
            Log.d("Http", it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(logger)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .url(
                        original.url.newBuilder()
                            .addQueryParameter("timestamp", System.currentTimeMillis().toString())
                            .build()
                    )

                val token = tokenStore.token
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }


                chain.proceed(requestBuilder.build())
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://music-api.cpolar.top")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(MusicApi::class.java)
    }


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppMemoryDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            AppMemoryDatabase::class.java,
        ).build()
    }

    @Provides
    @Singleton
    fun provideCommentDao(appDatabase: AppMemoryDatabase): CommentDao {
        return appDatabase.commentDao()
    }
}