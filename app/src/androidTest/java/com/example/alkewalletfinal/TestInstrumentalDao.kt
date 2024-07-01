package com.example.alkewalletfinal

import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import com.example.alkewalletfinal.model.local.WalletDao
import com.example.alkewalletfinal.model.local.WalletDataBase
import com.example.alkewalletfinal.model.local.entities.AccountsLocal
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestInstrumentalDao {

    private lateinit var db: WalletDataBase
    private lateinit var walletDao: WalletDao

    @Before
    fun setUp(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, WalletDataBase::class.java).build()
        walletDao = db.getWalletDao()
    }

    @After
    fun tearDown(){
        db.close()
    }

    @Test
    fun testDao() = runBlocking {
        //Crear una cuenta de prueba
        val cuenta = AccountsLocal(
            id = 1,
            creationDate = "25-06-2024",
            money = "5000",
            isBlocked = false,
            userId = 1,
            createdAt = "26-06-2024",
            updatedAt = "27-06-2024")

        //Insertar la cuenta en la base de datos
        walletDao.insertAccounts(cuenta)

        //Obtener la cuenta por su id
        val cuentaDesdeBD = walletDao.getCuentas(1)

        runOnUiThread {
            // Crear el observador para la cuenta
            val observadorCuenta = Observer<AccountsLocal> { cuentaObtenida ->
                assertNotNull(cuentaObtenida)
                assertEquals(cuenta.id, cuentaObtenida.id)
                assertEquals(cuenta.creationDate, cuentaObtenida.creationDate)
                assertEquals(cuenta.money, cuentaObtenida.money)
                assertEquals(cuenta.isBlocked, cuentaObtenida.isBlocked)
                assertEquals(cuenta.userId, cuentaObtenida.userId)
                assertEquals(cuenta.createdAt, cuentaObtenida.createdAt)
                assertEquals(cuenta.updatedAt, cuentaObtenida.updatedAt)
            }

            // Observar la cuenta y verificar los resultados
            cuentaDesdeBD.observeForever(observadorCuenta)

            // Quitar el observador después de realizar las aserciones
            cuentaDesdeBD.removeObserver(observadorCuenta)

        }

    }

}