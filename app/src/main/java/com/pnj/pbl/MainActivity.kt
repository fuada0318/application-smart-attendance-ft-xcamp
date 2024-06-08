package com.pnj.pbl

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pnj.pbl.data.PrefManager

class MainActivity : AppCompatActivity() {

    private lateinit var profil : PrefManager

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

    private fun checkSession(){

        profil = PrefManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            if (profil.getLogin()){
                val pindahHalaman = Intent(this,HomePage::class.java)
                startActivity(pindahHalaman)
                finish()
            }else{
                val pindahHalaman = Intent(applicationContext,LoginPage::class.java)
                startActivity(pindahHalaman)
                finish()
            }
        },1500)
    }
}