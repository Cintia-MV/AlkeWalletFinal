package com.example.alkewalletfinal.model.response

import com.google.gson.annotations.SerializedName

/**
 * Clase que representa la solicitud de creación de usuario enviada a la API.
 * @author Cintia Muñoz V.
 */
data class UserRequest(

    @SerializedName("first_name")
    val nombre: String,
    @SerializedName("last_name")
    val apellido: String,
    val email:String?,
    val password: String?,
    val roleId: Int
)
