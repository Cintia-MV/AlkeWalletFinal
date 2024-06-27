package com.example.alkewalletfinal.model.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tabla_transaccion")
data class TransactionsLocal(
    @PrimaryKey
    val id: Long,
    val amount: String,
    val concept: String,
    val date: String?,
    val type: String,
    val accountId: Long,
    val userId: Long,
    @SerializedName("to_account_id")
    val toAccountId: Long,
    val createdAt: String,
    val updatedAt: String
)
