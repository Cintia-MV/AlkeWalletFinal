package com.example.alkewalletfinal.model

import com.google.gson.annotations.SerializedName

data class UserRequest(

    @SerializedName("first_name")
    val nombre: String,
    @SerializedName("last_name")
    val apellido: String,
    val email:String?,
    val password: String?,
    val roleId: Int
)
