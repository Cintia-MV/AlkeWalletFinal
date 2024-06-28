package com.example.alkewalletfinal.model

import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal
import com.example.alkewalletfinal.model.response.AccountsResponse
import com.example.alkewalletfinal.model.response.Transactions
import com.example.alkewalletfinal.model.response.UserResponse

/**
 * Convierte un objeto de respuesta de usuario (`UserResponse`) en un objeto local de usuario (`UsuarioLocal`).
 * @author Cintia Muñoz V.
 *
 */
fun usuarioResponseInter(usuario: UserResponse): UsuarioLocal {
    return UsuarioLocal(
        id = usuario.id,
        firstName = usuario.firstName,
        lastName = usuario.lastName,
        email = usuario.email,
        password = usuario.password,
        points = usuario.points,
        roleid = usuario.roleid,
        createdAt = usuario.createdAt,
        updatedAt = usuario.updateAt ?: ""
    )
}

/**
 * Convierte un objeto de respuesta de cuenta (`AccountsResponse`) en un objeto local de cuenta (`AccountsLocal`).
 *
 */
fun cuentaResponseInter(cuenta: AccountsResponse): AccountsLocal{
    return AccountsLocal(
        id = cuenta.id,
        creationDate = cuenta.creationDate,
        money = cuenta.money,
        isBlocked = cuenta.isBlocked,
        userId = cuenta.userId,
        createdAt = cuenta.createdAt,
        updatedAt = cuenta.updatedAt

    )
}

/**
 * Convierte un objeto de respuesta de transacción (`Transactions`) en un objeto local de transacción (`TransactionsLocal`).
 *
 */
fun transactionResponseInter(transaccion: Transactions): TransactionsLocal{
    return TransactionsLocal(
        id = transaccion.id,
        amount = transaccion.amount,
        concept = transaccion.concept,
        date = transaccion.date ?: "",
        type = transaccion.type,
        accountId = transaccion.accountId,
        userId = transaccion.userId,
        toAccountId = transaccion.toAccountId,
        createdAt = transaccion.createdAt,
        updatedAt = transaccion.updatedAt
    )
}