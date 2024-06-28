package com.example.alkewalletfinal.model.response

/**
 * Clase que representa la respuesta de cuenta recibida desde la API.
 * @author Cintia Muñoz V.
 */
data class AccountsResponse(
    val id: Long,
    val creationDate: String,
    val money: String,
    val isBlocked: Boolean,
    val userId: Long,
    val createdAt: String,
    val updatedAt: String
)