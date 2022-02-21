package com.example.den.gituserrxjava.model

data class GitUser(
    val login: String,
    val id: String,
    val avatar_url: String,
    val url: String,
    val html_url: String,
    val followers_url: String,
    val following_url: String,
    val starred_url: String,
    val gists_url: String,
    val type: String,
    val score: String
)
