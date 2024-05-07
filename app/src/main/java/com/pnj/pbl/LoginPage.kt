package com.pnj.pbl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginPage : AppCompatActivity() {

    lateinit var btnLogin: Button
    lateinit var dataUsername: EditText
    lateinit var dataPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnLogin = findViewById(R.id.btnLogin)
        dataUsername = findViewById(R.id.getUsername)
        dataPassword = findViewById(R.id.getPasswd)
        btnLogin.setOnClickListener(this)
        btnLogin.setOnClickListener {
            if(dataUsername.text.toString().equals("joko") && dataPassword.text.toString().equals("p")){
                val pindahHalaman = Intent(this,HomePageUser::class.java)
                startActivity(pindahHalaman)
            } else{
                Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private fun Button.setOnClickListener(loginPage: LoginPage) {

}
