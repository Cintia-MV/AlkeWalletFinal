package com.example.alkewalletfinal.model.response

data class Usuario(
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String,
    val roleId: Int,
    val points: Int
)