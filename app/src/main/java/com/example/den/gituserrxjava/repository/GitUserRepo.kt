package com.example.den.gituserrxjava.repository

import android.util.Log
import android.widget.ImageView
import com.example.den.gituserrxjava.R
import com.example.den.gituserrxjava.api.GitApi
import com.example.den.gituserrxjava.model.GitUser
import com.example.den.gituserrxjava.model.GitUserDetail
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "GitRepo"

class GitUserRepo {
    private val gitApi: GitApi
    init {
        Log.i(TAG,"Create Repo")
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            //.addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("https://api.github.com/")
            .build()
        gitApi = retrofit.create(GitApi::class.java)
    }
    fun getUsers(): Observable<MutableList<GitUser>> {
        return gitApi.getUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
    fun getUserDetail(name : String) : Single<GitUserDetail> {
        return gitApi.getUserDetail(name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
    fun getAvatar(url : String) : Single<ResponseBody> {
        return gitApi.getAvatar(url)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun getBitmapAvatar(url : String, imageView: ImageView){
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error_404)
            .into(imageView)
    }
    fun getPage(since : String) : Observable<MutableList<GitUser>> {
        return gitApi.getNextPage(since)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}