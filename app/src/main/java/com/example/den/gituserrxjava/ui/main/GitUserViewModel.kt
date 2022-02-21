package com.example.den.gituserrxjava.ui.main

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.den.gituserrxjava.model.GitUser
import com.example.den.gituserrxjava.model.GitUserDetail

class GitUserViewModel : BaseObservable() {
    var user: GitUser? = null
        set(user) {
            field = user
            notifyChange()  //часть BaseObservable, говорит обновить все данные
            //notifyPropertyChanged(BR.title) //часть BaseObservable, говорит обновить только title. Если этого не делать тут то при повороте холдеры в рециклере будут рандомно меняться местами. По идее вызывается когда своиство изменилось
        }
    var userDetail : GitUserDetail? = null
        set(userDetail){
            field = userDetail
            notifyChange()
        }

    @get:Bindable   //аннотация BaseObservable, говорит наблюдать за этим свойством, если оно изменилось то вызовится сеттер где я и обновлю данные
    val userLogin: String?
        get() = user?.login

    @get:Bindable
    val userID: String?
        get() = user?.id + " - "

    @get:Bindable
    val userName: String?
        get() = userDetail?.name

    @get:Bindable
    val userCompany: String?
        get() = userDetail?.company


}