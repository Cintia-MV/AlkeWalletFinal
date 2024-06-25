package com.example.alkewalletfinal.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import com.example.alkewalletfinal.model.local.entities.TransactionsLocal
import com.example.alkewalletfinal.model.local.entities.UsuarioLocal

@Database(entities = [UsuarioLocal::class, TransactionsLocal::class, AccountsLocal::class], version = 1, exportSchema = false)
abstract class WalletDataBase : RoomDatabase() {

    abstract fun getWalletDao(): WalletDao

    companion object{
        @Volatile
        private var INSTANCE: WalletDataBase? = null

        fun getDataBase(context: Context): WalletDataBase{
            val tempInstance = INSTANCE
            if(tempInstance!= null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WalletDataBase::class.java,
                    "wallet_BD"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}