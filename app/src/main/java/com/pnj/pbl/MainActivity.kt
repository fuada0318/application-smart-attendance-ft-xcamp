package com.pnj.pbl

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkSession()
    }

    fun checkSession(){
        var login = false
        Handler(Looper.getMainLooper()).postDelayed({
            if (login){
                val pindahHalaman = Intent(this,HomePageUser::class.java)
                startActivity(pindahHalaman)
            }else{
                val pindahHalaman = Intent(applicationContext,LoginPage::class.java)
                startActivity(pindahHalaman)
            }
        },1500)
    }
}