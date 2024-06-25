package com.example.alkewalletfinal.model.response

data class LoginRequest(val email:String, val password: String){
    companion object {

            const val emailTemp = "arnold.s@mail.com"
            const val passwordTemp = "arnold123"

    }
}
