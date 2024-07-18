package com.pnj.pbl

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pnj.pbl.api.RetrofitClient
import com.pnj.pbl.data.PrefManager
import com.pnj.pbl.data.ResponseLogin
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val email = profil.getEmail().toString()
        val pwd = profil.getPasswd().toString()

        Handler(Looper.getMainLooper()).postDelayed({
            if (profil.getLogin()){
                loginUlang(email,pwd)
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

    private fun loginUlang(email:String, pwd:String){
        val api = RetrofitClient().getDataAPI()
        api.postLogin(email,pwd).enqueue(object : Callback<ResponseLogin>{
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful){
                    profil.setEmail(response.body()!!.data.user.email)
                    profil.setPassword(pwd)
                    profil.setName(response.body()!!.data.user.name)
                    profil.setProfile(response.body()!!.data.user.profile_pict_url)
                    profil.setId(response.body()!!.data.user.user_id)
                    profil.setFloor(response.body()!!.data.user.floor)
                    profil.setToken(response.body()!!.token)
                    profil.setLogin(true)
                } else{
//                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
//                    val msgerr = jsonObj.getString("message")
//                    Toast.makeText(this@MainActivity, "$msgerr", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("Error Login", "${t.message}")
            }

        })
    }
}