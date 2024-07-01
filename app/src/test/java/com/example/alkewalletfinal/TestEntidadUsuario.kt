package com.example.alkewalletfinal

import com.example.alkewalletfinal.model.local.entities.UsuarioLocal
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TestEntidadUsuario {

    //Variable de la entidad
    private lateinit var usuario: UsuarioLocal

    //Función que se ejecuta antes de cada prueba
    @Before
    fun setUp(){
        //Entidad inicializada con valores de prueba
        usuario = UsuarioLocal(
            id = 100L,
            firstName = "Juan",
            lastName = "Parra",
            email = "jp@mail.com",
            password = "jp1234",
            points = 50,
            roleid = 1,
            createdAt = "25-06-2024",
            updatedAt = null
        )
    }

    @After
    fun tearDown(){

    }

    @Test
    fun testUsuario(){
        assert(usuario.id == 100L)
        assert(usuario.firstName == "Juan")
        assert(usuario.lastName == "Parra")
        assert(usuario.email == "jp@mail.com")
        assert(usuario.password == "jp1234")
        assert(usuario.points == 50)
        assert(usuario.roleid == 1)
        assert(usuario.createdAt == "25-06-2024")
        assert(usuario.updatedAt == null)
    }

}