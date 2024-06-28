package com.example.alkewalletfinal.model.response

/**
 * Clase que representa la respuesta de transacciones recibida desde la API.
 * @author Cintia Muñoz V.
 */
data class TransactionsResponse(
    val data: List<Transactions>
)
