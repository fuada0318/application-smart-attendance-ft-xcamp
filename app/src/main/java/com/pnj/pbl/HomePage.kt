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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pnj.pbl.api.RetrofitClient
import com.pnj.pbl.data.PrefManager
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
    lateinit var btnView: Button
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var listAttendance:RecyclerView
    lateinit var imgProfile: CircleImageView
    lateinit var imgStatus: ImageView
    lateinit var tvStatus: TextView

    lateinit var profil : PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page_test)
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

        profil = PrefManager(this)
        val tokenJWT = "Bearer ${profil.getToken()}"

        val btnLogout = findViewById<Button>(R.id.btnLogout)

//      Greetings
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greetingText: String = when (currentHour) {
            in 6..10 -> "Good Morning,"
            in 11..14 -> "Good Afternoon,"
            in 15..18 -> "Good Evening,"
            else -> "Good Night,"
        }
        tvGreet.text = greetingText
        tvName.text = profil.getName()

//      Profile Image
        Picasso.get().load(profil.getProfile()).into(imgProfile)

//        imgProfile.setOnClickListener {
//            startActivity(Intent(this,UpdateProfile::class.java))
//        }

//      Date
        val shift = "(08:00 - 16:00)"
        val today = formatDate(Date())
        val tDate = "$today $shift"
        tvDate.text = tDate

//      Check Attendance
        getAttendanceStts(tokenJWT)

//      ViewAll
        btnView.setOnClickListener {
            startActivity(Intent(this,AttendanceLog::class.java))
        }
//      Test Button Logout
        btnLogout.setOnClickListener {
            profil.logOut()

            startActivity(Intent(this,LoginPage::class.java))
            finish()
        }
        initView()
    }

    private fun initView() {
        swipeLayout = findViewById(R.id.main)
        listAttendance = findViewById(R.id.rcvAttendance)
    }

    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    private fun getAttendanceStts(token : String){
        val api = RetrofitClient().getAttStts()
        api.getAttendanceStatus(token).enqueue(object : Callback<ResponseAttendanceStatus>{
            override fun onResponse(
                call: Call<ResponseAttendanceStatus>,
                response: Response<ResponseAttendanceStatus>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.attendance_status == "absent"){
                        imgStatus.setImageResource(R.drawable.info)
                        tvStatus.text = "Anda Belum Presensi"
                    } else{
                        imgStatus.setImageResource(R.drawable.done)
                        tvStatus.text = "Anda Sudah Presensi"
                    }
                } else{
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    val msgerr = jsonObj.getString("detail")

                    showToast(msgerr)
                }
            }

            override fun onFailure(call: Call<ResponseAttendanceStatus>, t: Throwable) {
                Log.e("Pesan error", "${t.message}")
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
