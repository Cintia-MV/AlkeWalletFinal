package com.example.alkewalletfinal.model.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tabla_usuario")
data class UsuarioLocal(
    @PrimaryKey
    val id: Long,
    @SerializedName("first_name")
    val nombre: String,
    @SerializedName("last_name")
    val apellido: String,
    val email:String,
    val password: String,
    val points: Int,
    val roleId: Int,
    val createdAt: String,
    val updatedAt: String
)