package com.example.alkewalletfinal.model.response

import com.google.gson.annotations.SerializedName

/**
 * Clase que representa la solicitud de creación de transacción enviada a la API.
 * @author Cintia Muñoz V.
 */
data class TransactionsRequest(
    val amount: Long,
    val concept: String,
    val type: String,
    val accountId: Long,
    val userId: Long,
    @SerializedName("to_account_id")
    val toAccountId: Long
)