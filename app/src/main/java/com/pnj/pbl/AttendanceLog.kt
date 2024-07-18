package com.pnj.pbl

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pnj.pbl.adapter.LogAdapter
import com.pnj.pbl.api.RetrofitClient
import com.pnj.pbl.data.PrefManager
import com.pnj.pbl.data.ResponseAttendanceFiltered
import com.pnj.pbl.data.ResponseAttendanceLog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class AttendanceLog : AppCompatActivity() {

    private lateinit var tvAutoComplete: AutoCompleteTextView
    private lateinit var tvClock : TextView
    private lateinit var tvLateClock : TextView
    private lateinit var tvNoClock : TextView
    private lateinit var btnBack : Button
    private lateinit var rcvLogs : RecyclerView

    private var arrayLogs : ArrayList<ResponseAttendanceFiltered.DataAtt> = ArrayList()
    private var rcvAdpt : LogAdapter = LogAdapter(arrayLogs)

    private lateinit var profil: PrefManager

    override fun onResume() {
        super.onResume()
        val monTH = resources.getStringArray(R.array.month)
        val arrayAdapter = ArrayAdapter(this@AttendanceLog, R.layout.list_dropdown, monTH)
        tvAutoComplete.setAdapter(arrayAdapter)

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        tvAutoComplete.setText(monTH[currentMonth], false)

        checkSession()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomePage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_attendance_log)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tvAutoComplete = findViewById(R.id.autoCompleteTV)
        tvClock = findViewById(R.id.tvClockIn)
        tvNoClock = findViewById(R.id.tvNoClock)
        tvLateClock = findViewById(R.id.tvLateClock)
        btnBack = findViewById(R.id.btnBack1)
        rcvLogs = findViewById(R.id.rcvLogs)

        profil = PrefManager(this)
        val tokenJWT = "Bearer ${profil.getToken()}"

        btnBack.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        rcvLogs.apply { layoutManager = LinearLayoutManager(this@AttendanceLog) }

        tvAutoComplete.setOnItemClickListener { _, _, position, _ ->
            val selectedMonth = position + 1 // Convert month position to month number (January = 1, etc.)
            getAttendance(tokenJWT, selectedMonth)
        }

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        getAttendance(tokenJWT, currentMonth)


    }

    private fun getAttendance(token : String, month : Int){
        arrayLogs.clear()
        val apiLog = RetrofitClient().getDataAPI()
        apiLog.getAttendanceFiltered(token, month).enqueue(object : Callback<ResponseAttendanceFiltered>{
            override fun onResponse(
                call: Call<ResponseAttendanceFiltered>,
                response: Response<ResponseAttendanceFiltered>
            ) {
                if (response.isSuccessful){

                    response.body()?.data?.let { dataList ->
                        for (i in dataList.reversed()) {
                            arrayLogs.add(i)

                        }
                    }

                    tvLateClock.text = rcvAdpt.getItemLate(arrayLogs).toString()
                    tvClock.text = rcvAdpt.itemCount.toString()
                    rcvLogs.adapter = rcvAdpt
                    rcvAdpt.notifyDataSetChanged()
                } else{
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val msgErr = jsonObj.getString("message")
                    Log.e("Error Attendance Log", msgErr)

                    if (msgErr.contains("NoneType")) {
                        showToast("Tidak ada data kehadiran")
                    } else if (msgErr.contains("belum tersedia")){
                        arrayLogs.clear()
                        tvLateClock.text = 0.toString()
                        tvClock.text = 0.toString()
                        rcvAdpt.notifyDataSetChanged()
                        showToast("$msgErr")
                    } else {
                        profil.logOut()

                        val intent = Intent(this@AttendanceLog, LoginPage::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        showToast("Session berakhir, silahkan login ulang")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseAttendanceFiltered>, t: Throwable) {
                Log.e("Error Attendance Log", "${t.message}")
            }

        })

    }

    private fun checkSession() {
        if (!profil.getLogin()) {
            profil.logOut()

            val intent = Intent(this, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showToast(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }
}