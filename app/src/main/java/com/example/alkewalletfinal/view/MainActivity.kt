package com.example.alkewalletfinal.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewalletfinal.databinding.ActivityMainBinding

/**
 * Actividad principal que aloja los fragmentos principales de la aplicación.
 * Esta actividad infla y configura la vista principal utilizando el binding de la actividad.
 * @author Cintia Muñoz V.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

    }
}