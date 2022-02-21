package com.example.den.gituserrxjava.ui.main

import androidx.lifecycle.ViewModel
import com.example.den.gituserrxjava.model.GitUser
import com.example.den.gituserrxjava.repository.GitUserRepo

class MainViewModel : ViewModel() {
    var users = mutableListOf<GitUser>()
    val gitRepo = GitUserRepo()
}