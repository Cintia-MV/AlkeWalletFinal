package com.example.alkewalletfinal.model.remote

import com.example.alkewalletfinal.model.response.AccountsResponse
import com.example.alkewalletfinal.model.response.LoginRequest
import com.example.alkewalletfinal.model.response.LoginResponse
import com.example.alkewalletfinal.model.response.TransactionsRequest
import com.example.alkewalletfinal.model.response.TransactionsResponse
import com.example.alkewalletfinal.model.response.UserRequest
import com.example.alkewalletfinal.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interfaz para definir las llamadas a la API de la wallet.
 * @author Cintia Muñoz V.
 */
interface Api {

    //Iniciar sesión en la app
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    //Obtener información del usuario logueado
    @GET("auth/me")
    fun infoUser(@Header("Authorization") token: String): Call<UserResponse>

    //Crear nuevousuario
    @POST("/users")
    fun createUser(@Body usuario: UserRequest): Call<UserRequest>

    //Información de las cuentas asociadas al usuario logueado
    @GET("accounts/me")
    fun infoAccounts(@Header("Authorization") token: String): Call<List<AccountsResponse>>

    //Lista de transacciones realizadas por el usuario
    @GET("transactions")
    fun infoTransactions(@Header("Authorization") token: String): Call<TransactionsResponse>

    //Crear nueva transacción
    @POST("transactions")
    fun createTransaction(@Header("Authorization") authToken: String, @Body transaccion: TransactionsRequest): Call<TransactionsResponse>
}