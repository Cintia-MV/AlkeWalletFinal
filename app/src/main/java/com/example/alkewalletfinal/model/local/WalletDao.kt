package com.example.alkewalletfinal.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(usuario: UsuarioLocal)

    @Query("SELECT * FROM tabla_usuario WHERE id= :id")
    fun getUsuarios(id: Long): LiveData<UsuarioLocal>

    @Query("SELECT id FROM tabla_usuario LIMIT 1 ")
    fun getUserId(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(cuenta: AccountsLocal)

    @Query("SELECT * FROM tabla_cuenta WHERE id= :id")
    fun getCuentas(id: Long): LiveData<AccountsLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaccion: TransactionsLocal)

    @Query("SELECT * FROM tabla_transaccion WHERE id= :id")
    fun getTransactions(id: Long): LiveData<TransactionsLocal>

    @Query("SELECT * FROM tabla_cuenta WHERE userId = :userId")
    fun getAccountsByUserId(userId: Long): LiveData<List<AccountsLocal>>

}