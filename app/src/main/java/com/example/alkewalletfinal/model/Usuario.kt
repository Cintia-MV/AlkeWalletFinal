package com.example.alkewalletfinal.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    val id: Long,
    @SerializedName("first_name")
    val nombre: String,
    @SerializedName("last_name")
    val apellido: String,
    val email:String?,
    val password: String?,
    val points: Int,
    val roleId: Int,
    val createdAt: String,
    val updatedAt: String
)