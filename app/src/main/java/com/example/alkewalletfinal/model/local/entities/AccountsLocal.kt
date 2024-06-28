package com.example.alkewalletfinal.model.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de la base de datos que representa una cuenta local.
 * @author Cintia Muñoz V.
 */

@Entity(tableName = "tabla_cuenta")
data class AccountsLocal(
    @PrimaryKey
    val id: Long,
    val creationDate: String,
    val money: String,
    val isBlocked: Boolean,
    val userId: Long,
    val createdAt: String,
    val updatedAt: String
)
