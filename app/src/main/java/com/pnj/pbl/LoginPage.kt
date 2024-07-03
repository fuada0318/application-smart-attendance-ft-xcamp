package com.pnj.pbl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
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

class LoginPage : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var dataEmail: EditText
    private lateinit var dataPassword: EditText
    private lateinit var progBar : ProgressBar

    private var emailUser : String = ""
    private var passwordUser : String = ""
    private lateinit var profil : PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//      Binding Activity
        btnLogin = findViewById(R.id.btnLogin)
        dataEmail = findViewById(R.id.emailEt)
        dataPassword = findViewById(R.id.passET)
        progBar = findViewById(R.id.loading)
        profil = PrefManager(this)

        btnLogin.setOnClickListener {
            emailUser = dataEmail.text.toString()
            passwordUser = dataPassword.text.toString()

            if (emailUser.isEmpty() || emailUser == ""){
                dataEmail.error = "Email tidak boleh kosong"
            } else if (!isValidEmail(emailUser)){
                dataEmail.error = "Email tidak valid"
            } else if (passwordUser.isEmpty() || passwordUser == ""){
                dataPassword.error = "Password tidak boleh kosong"
            } else{
                progBar.visibility = View.VISIBLE
                getLogin()
            }
        }
    }

    private fun getLogin() {
        val api = RetrofitClient().getDataUser()
        api.postLogin(emailUser, passwordUser).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {
                    profil.setEmail(response.body()!!.data.user.email)
                    profil.setName(response.body()!!.data.user.name)
                    profil.setProfile(response.body()!!.data.user.profile_pict_url)
                    profil.setId(response.body()!!.data.user.user_id)
                    profil.setFloor(response.body()!!.data.user.floor)
                    profil.setToken(response.body()!!.token)
                    profil.setLogin(true)

                    progBar.visibility = View.GONE
                    showToast("Login Berhasil!")
                    startActivity(Intent(this@LoginPage, HomePage::class.java))
                    finish()
                } else {
                    progBar.visibility = View.GONE
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val msgerr = jsonObj.getString("message")

                    showToast(msgerr)
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("Error Login", "${t.message}")
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginPage, message, Toast.LENGTH_LONG).show()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }
}
