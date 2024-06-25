package com.example.alkewalletfinal.model.response

import com.google.gson.annotations.SerializedName

data class TransactionsRequest(
    val amount: Long,
    val concept: String,
    val type: String,
    val accountId: Long,
    val userId: Long,
    @SerializedName("to_account_id")
    val toAccountId: Long
)