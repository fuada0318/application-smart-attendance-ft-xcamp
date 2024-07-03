package com.pnj.pbl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pnj.pbl.data.PrefManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProfilePage : AppCompatActivity() {
    private lateinit var imgProfil: CircleImageView
    private lateinit var btnLogout : Button
    private lateinit var btnUpdate : Button
    private lateinit var txUserid : TextView
    private lateinit var txNama : TextView
    private lateinit var txEmail : TextView

    private lateinit var profil : PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnUpdate = findViewById(R.id.btnProfile)
        btnLogout = findViewById(R.id.btnLogout)
        imgProfil = findViewById(R.id.imgProfil)
        txUserid = findViewById(R.id.txUserid)
        txNama = findViewById(R.id.txNama)
        txEmail = findViewById(R.id.txEmail)

        profil = PrefManager(this)
        val tokenJWT = "Bearer ${profil.getToken()}"

//      Menampilkan Data
        Picasso.get().load(profil.getProfile()).into(imgProfil)
        txUserid.text= profil.getId().toString()
        txNama.text= profil.getName()
        txEmail.text= profil.getEmail()
//      Fungsi update profile
        btnUpdate.setOnClickListener {
            Toast.makeText(this, "Fitur belum ada", Toast.LENGTH_SHORT).show()
        }
//      Fungsi LogOut
        btnLogout.setOnClickListener {
            profil.logOut()

            startActivity(Intent(this,LoginPage::class.java))
            finish()
        }
    }
}