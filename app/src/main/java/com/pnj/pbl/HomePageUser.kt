package com.pnj.pbl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



class HomePageUser : AppCompatActivity() {
    lateinit var btnView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnView = findViewById(R.id.btnViewAll)
//        btnView.setOnClickListener(this)
        btnView.setOnClickListener {
//            Intent(this,AttendanceLogsUser::class.java)
            startActivity(Intent(this,AttendanceLogUser::class.java))
        }
    }
}

private fun Button.setOnClickListener(homePageUser: HomePageUser) {

}
