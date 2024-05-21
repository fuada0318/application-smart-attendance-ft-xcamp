package com.pnj.pbl

import android.content.Intent
import android.content.SharedPreferences
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
import com.pnj.pbl.API.RetrofitClient
import com.pnj.pbl.data.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPage : AppCompatActivity() {

    lateinit var btnLogin: Button
    lateinit var dataEmail: EditText
    lateinit var dataPassword: EditText
    lateinit var progBar : ProgressBar

    private var emailUser : String = ""
    private var passwordUser : String = ""

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
        dataEmail = findViewById(R.id.getEmail)
        dataPassword = findViewById(R.id.getPasswd)
        progBar = findViewById(R.id.loading)

        btnLogin.setOnClickListener(this)
        btnLogin.setOnClickListener {
            emailUser = dataEmail.text.toString()
            passwordUser = dataPassword.text.toString()

            when {
                emailUser == "" -> {
                    dataEmail.error = "Email tidak boleh kosong"
                }
                passwordUser == "" -> {
                    dataPassword.error = "Password tidak boleh kosong"
                }
                else -> {
                    progBar.visibility = View.VISIBLE
                    getLogin()
                }
            }
        }
    }

    private fun getLogin() {
        val api = RetrofitClient().getDataUser()
        api.postLogin(emailUser, passwordUser).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.operation_status != 1) {
                        progBar.visibility = View.GONE
                        showToast("Login Gagal!")
                    } else {
//                        val jwtToken : String = response.body()!!.token

//                      Membuat Session
//                        val loginResponse = LoginResponse.data.user
                        getSharedPreferences("login_session", MODE_PRIVATE)
                            .edit()
                            .putString("email", response.body()?.data?.user?.email)
                            .putInt("floor", response.body()?.data?.user?.floor!!)
                            .putString("name", response.body()?.data?.user?.name)
                            .putString("profile", response.body()?.data?.user?.profile_pict_url)
                            .putInt("role", response.body()?.data?.user?.role!!)
                            .putLong("id", response.body()?.data?.user?.user_id!!)
                            .putString("jwt_token", response.body()?.token)
                            .apply()

                        progBar.visibility = View.GONE
                        showToast("Login Berhasil!")
                        startActivity(Intent(this@LoginPage, HomePage::class.java))
                        finish()
                    }
                } else {
                    showToast("Login Error!")
//                    val msgerr : Int = response.body()!!.operation_status
//                    Toast.makeText(this@LoginPage, msgerr, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Pesan error", "${t.message}")
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginPage, message, Toast.LENGTH_LONG).show()
    }
}

private fun Button.setOnClickListener(loginPage: LoginPage) {

}
