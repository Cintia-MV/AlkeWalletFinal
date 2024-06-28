package com.example.alkewalletfinal.model.response

import com.google.gson.annotations.SerializedName

/**
 * Clase que representa una transacción recibida desde la API
 * @author Cintia Muñoz V.
 */
data class Transactions(
    val id: Long,
    val amount: String,
    val concept: String,
    val date: String,
    val type: String,
    val accountId: Long,
    val userId: Long,
    @SerializedName("to_account_id")
    val toAccountId: Long,
    val createdAt: String,
    val updatedAt: String
)
