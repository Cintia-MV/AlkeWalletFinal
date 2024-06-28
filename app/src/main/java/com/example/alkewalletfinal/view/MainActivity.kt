package com.example.alkewalletfinal.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //Instancia del binding
    private lateinit var mbinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)

    }
}