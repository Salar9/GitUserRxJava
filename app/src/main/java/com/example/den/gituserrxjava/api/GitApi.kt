package com.example.den.gituserrxjava.api

import com.example.den.gituserrxjava.model.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface GitApi {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    fun getUsers(): Observable<MutableList<GitUser>>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users/{username}")
    fun getUserDetail(@Path("username") username: String): Single<GitUserDetail>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/users")
    fun getNextPage(@Query("since") query : String): Observable<MutableList<GitUser>>

    @GET
    fun getAvatar(@Url url: String): Single<ResponseBody>
}