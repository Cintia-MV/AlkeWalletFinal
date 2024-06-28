package com.example.alkewalletfinal.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal

/**
 * Base de datos de Room para la aplicación de wallet.
 *
 * @property getWalletDao Método abstracto para obtener el DAO de la wallet.
 * @author Cintia Muñoz V.
 */
@Database(entities = [UsuarioLocal::class, TransactionsLocal::class, AccountsLocal::class], version = 1, exportSchema = true)
abstract class WalletDataBase : RoomDatabase() {

    //Obtiener el dao
    abstract fun getWalletDao(): WalletDao

    companion object{
        @Volatile
        private var INSTANCE: WalletDataBase? = null

        //Instancia de la base de datos
        fun getDataBase(context: Context): WalletDataBase{
            val tempInstance = INSTANCE
            if(tempInstance!= null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WalletDataBase::class.java,
                    "bd_wallet"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}