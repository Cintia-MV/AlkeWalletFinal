package com.example.alkewalletfinal.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal

/**
 * DAO para realizar operaciones en la base de datos de la wallet.
 * @author Cintia Muñoz V.
 */
@Dao
interface WalletDao {

    /**
     * Inserta un nuevo usuario en la base de datos. Si el usuario ya existe, lo reemplaza.
     *
     * @param usuario El usuario a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(usuario: UsuarioLocal)

    /**
     * Obtiene un usuario de la base de datos por su ID.
     *
     * @param id El ID del usuario.
     * @return Un LiveData que contiene el usuario.
     */
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

    /**
     * Obtiene todas las cuentas de la base de datos asociadas a un usuario específico por su ID de usuario.
     *
     * @param userId El ID del usuario.
     * @return Un LiveData que contiene una lista de cuentas asociadas al usuario.
     */
    @Query("SELECT * FROM tabla_cuenta WHERE userId = :userId")
    fun getAccountsByUserId(userId: Long): LiveData<List<AccountsLocal>>

    /**
     * Obtiene todas las transacciones de la base de datos asociadas a un usuario específico por su ID de usuario.
     *
     * @param userId El ID del usuario.
     * @return Un LiveData que contiene una lista de transacciones asociadas al usuario.
     */
    @Query("SELECT * FROM tabla_transaccion WHERE userId = :userId")
    fun getTransactionsByUserId(userId: Long): LiveData<List<TransactionsLocal>>

}