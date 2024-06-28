package com.example.alkewalletfinal.model.response

import com.google.gson.annotations.SerializedName

/**
 * Clase que representa la respuesta de usuario recibida desde la API.
 * @author Cintia Muñoz V.
 */
data class UserResponse(
    val id: Long,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val points: Int,
    val roleid: Int,
    val createdAt: String,
    val updateAt: String
)
