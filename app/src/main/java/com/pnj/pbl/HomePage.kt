package com.pnj.pbl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pnj.pbl.adapter.HomeLogAdapter
import com.pnj.pbl.api.RetrofitClient
import com.pnj.pbl.data.PrefManager
import com.pnj.pbl.data.ResponseAttendanceLog
import com.pnj.pbl.data.ResponseAttendanceStatus
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomePage : AppCompatActivity() {
    private lateinit var btnView: Button
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var rcvAtt: RecyclerView
    private lateinit var imgProfile: CircleImageView
    private lateinit var imgStatus: ImageView
    private lateinit var tvStatus: TextView
    private lateinit var tvTest: TextView

    private var arrayAtt: ArrayList<ResponseAttendanceLog.DataAtt> = ArrayList()
    private var rcvAdapter: HomeLogAdapter = HomeLogAdapter(arrayAtt)
    private lateinit var profil: PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//      Binding Activity
        val tvGreet = findViewById<TextView>(R.id.tvGreet)
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        imgProfile = findViewById(R.id.imgProfil)
        imgStatus = findViewById(R.id.imgStatus)
        tvStatus = findViewById(R.id.tvStatus)
        btnView = findViewById(R.id.btnViewAll)
        swipeLayout = findViewById(R.id.main)
        rcvAtt = findViewById(R.id.rcvAttendance)
        tvTest = findViewById(R.id.testest)


        profil = PrefManager(this)
        val tokenJWT = "Bearer ${profil.getToken()}"

        tvTest.text = profil.getTokenFCM()


//      Greetings
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greetingText: String = when (currentHour) {
            in 4..10 -> "Good Morning,"
            in 11..14 -> "Good Afternoon,"
            in 15..19 -> "Good Evening,"
            else -> "Good Night,"
        }
        tvGreet.text = greetingText
        tvName.text = profil.getName()

//      Profile Image
        Picasso.get().load(profil.getProfile()).into(imgProfile)

        imgProfile.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

//      Date
        val shift = "(08:00 - 16:00)"
        val today = formatDate(Date())
        val tDate = "$today $shift"
        tvDate.text = tDate

//      Check Attendance
        getAttendanceStts(tokenJWT)

//      ViewAll
        btnView.setOnClickListener {
            val intent = Intent(this, AttendanceLog::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

//      Attendance Logs
        rcvAtt.apply {
            layoutManager = LinearLayoutManager(this@HomePage)
        }
        getAttendanceLog(tokenJWT)

//      Refresh Layout
        swipeLayout.setOnRefreshListener {
            refreshPage(tokenJWT)
        }
    }

    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    private fun refreshPage(token: String) {
        swipeLayout.isRefreshing = true
        swipeLayout.postDelayed({
            Picasso.get().load(profil.getProfile()).into(imgProfile)
            getAttendanceLog(token)
            getAttendanceStts(token)

            swipeLayout.isRefreshing = false
        }, 3000)
    }

    private fun getAttendanceStts(token: String) {
        val api = RetrofitClient().getDataAPI()
        api.getAttendanceStatus(token).enqueue(object : Callback<ResponseAttendanceStatus> {
            override fun onResponse(
                call: Call<ResponseAttendanceStatus>,
                response: Response<ResponseAttendanceStatus>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.attendance_status == "present") {
                        imgStatus.setImageResource(R.drawable.done)
                        tvStatus.text = "Anda Sudah Presensi"
                    } else {
                        imgStatus.setImageResource(R.drawable.info)
                        tvStatus.text = "Anda Belum Presensi"
                    }
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val msgErr = jsonObj.getString("message")
                    Log.e("Error Attendance Status", msgErr)

                    if (msgErr.contains("NoneType")) {
                        imgStatus.setImageResource(R.drawable.info)
                        tvStatus.text = "Anda Belum Presensi"
                    } else {
                        logOut()
                        showToast("Session berakhir, silahkan login ulang")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseAttendanceStatus>, t: Throwable) {
                Log.e("Error Attendance Status", "${t.message}")
            }

        })
    }

    private fun getAttendanceLog(token: String) {
        arrayAtt.clear()
        val getLog = RetrofitClient().getDataAPI()
        getLog.getAttendanceLogs(token).enqueue(object : Callback<ResponseAttendanceLog> {
            override fun onResponse(
                call: Call<ResponseAttendanceLog>,
                response: Response<ResponseAttendanceLog>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { dataList ->
                        for (i in dataList.reversed()) {
                            if (arrayAtt.size < 5) {
                                arrayAtt.add(i)
                            }
                        }
                    }
                    rcvAtt.adapter = rcvAdapter
                    rcvAdapter.notifyDataSetChanged()
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val msgErr = jsonObj.getString("message")
                    Log.e("Error Attendance Home", msgErr)

                    if (msgErr.contains("NoneType")) {
                        showToast("Tidak ada data kehadiran")
                    } else {
                        logOut()
                        showToast("Session berakhir, silahkan login ulang")
                    }

                }
            }

            override fun onFailure(call: Call<ResponseAttendanceLog>, t: Throwable) {
                Log.e("Error Attendance Home", "${t.message}")
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
