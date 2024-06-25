package com.example.alkewalletfinal.model.remote

import com.example.alkewalletfinal.model.response.LoginRequest
import com.example.alkewalletfinal.model.response.LoginResponse
import com.example.alkewalletfinal.model.response.UserResponse
import com.example.alkewalletfinal.model.response.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {

    //Login
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("auth/me")
    fun infoUser(@Header("Authorization") token: String): Call<UserResponse>

    //Crear usuario
    @POST("/users")
    fun createUser(@Body usuario: Usuario): Call<Usuario>
}