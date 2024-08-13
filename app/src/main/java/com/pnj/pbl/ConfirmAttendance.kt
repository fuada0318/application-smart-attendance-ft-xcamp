package com.pnj.pbl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pnj.pbl.adapter.ConfirmAdapter
import com.pnj.pbl.adapter.HomeLogAdapter
import com.pnj.pbl.api.RetrofitClient
import com.pnj.pbl.data.PrefManager
import com.pnj.pbl.data.ResponseAttendanceLog
import com.pnj.pbl.data.ResponseDeleteData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConfirmAttendance : AppCompatActivity() {
    private lateinit var btnNo: Button
    private lateinit var btnYes: Button
    private lateinit var tvData: TextView
    private lateinit var rcvConf: RecyclerView

    private var arrayConf: ArrayList<ResponseAttendanceLog.DataAtt> = ArrayList()
    private var rcvAdpt: ConfirmAdapter = ConfirmAdapter(arrayConf)
    private lateinit var profil: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirm_attendance)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnNo = findViewById(R.id.btnNo)
        btnYes = findViewById(R.id.btnYes)
        tvData = findViewById(R.id.textDCF)
        rcvConf = findViewById(R.id.rcvConf)
        profil = PrefManager(this)
        val tokenJWT = "Bearer ${profil.getToken()}"
        val userId = profil.getId()!!

        val intent = Intent(this, HomePage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        if (intent.action == "OPEN_ATTENDANCE_ACTIVITY") {

        }
        rcvConf.apply {
            layoutManager = LinearLayoutManager(this@ConfirmAttendance)
        }
        getAttendanceNew(tokenJWT)

        btnNo.setOnClickListener {
            deleteData(tokenJWT, userId)

            startActivity(intent)
            finish()
        }
        btnYes.setOnClickListener {
            showToast("Verifikasi berhasil")
            startActivity(intent)
            finish()
        }
    }

    private fun deleteData(token: String, user_id: Long) {
        val Api = RetrofitClient().getDataAPI()
        Api.delDataAtt(token, user_id).enqueue(object : Callback<ResponseDeleteData> {
            override fun onResponse(
                call: Call<ResponseDeleteData>,
                response: Response<ResponseDeleteData>
            ) {
                if (response.isSuccessful) {
                    showToast("Verifikasi berhasil")
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val msgErr = jsonObj.getString("message")
                    Log.e("Error Delete Data", msgErr)

                    if (msgErr.contains("attendance data is not yet available")) {
                        showToast("Tidak ada data")
                    } else if (msgErr.contains("NoneType")) {
                        showToast("Tidak ada data")
                    } else {
                        logOut()
                        showToast("Session berakhir, silahkan login ulang")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDeleteData>, t: Throwable) {
                Log.e("Error Delete Data", "${t.message}")
            }

        })
    }

    private fun getAttendanceNew(token: String) {
        arrayConf.clear()
        val today = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date())
        val getLog = RetrofitClient().getDataAPI()
        getLog.getAttendanceLogs(token).enqueue(object : Callback<ResponseAttendanceLog> {
            override fun onResponse(
                call: Call<ResponseAttendanceLog>,
                response: Response<ResponseAttendanceLog>
            ) {
                if (response.isSuccessful) {
                    btnYes.visibility = View.VISIBLE
                    btnNo.visibility = View.VISIBLE
                    rcvConf.visibility = View.VISIBLE
                    tvData.visibility = View.GONE
                    for (i in response.body()!!.data) {
                        val dateTimeParts = i.timestamp.split(" ")
                        val date = dateTimeParts.take(4).joinToString(" ")

                        if (date == today) {
                            arrayConf.add(i)
                        }
                    }
                    rcvConf.adapter = rcvAdpt
                    rcvAdpt.notifyDataSetChanged()
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val msgErr = jsonObj.getString("message")
                    Log.e("Error Attendance Log", msgErr)

                    if (msgErr.contains("NoneType")) {
                        tvData.visibility = View.VISIBLE
                    } else {
                        logOut()
                        showToast("Session berakhir, silahkan login ulang")
                    }

                }
            }

            override fun onFailure(call: Call<ResponseAttendanceLog>, t: Throwable) {
                Log.e("Error Attendance Log", "${t.message}")
            }

        })
    }

    private fun logOut() {
        profil.logOut()

        val intent = Intent(this, LoginPage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }
}