package com.pnj.pbl

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
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
    lateinit var profil : SharedPreferences

    val present = true

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
        val tvName =findViewById<TextView>(R.id.tvName)
        imgProfile = findViewById(R.id.imgProfil)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        imgStatus = findViewById(R.id.imgStatus)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        btnView = findViewById(R.id.btnViewAll)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        profil = getSharedPreferences("login_session", MODE_PRIVATE)

//      Greetings
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greetingText: String = when (currentHour) {
            in 6..11 -> "Good Morning,"
            in 12..17 -> "Good Afternoon,"
            in 18..20 -> "Good Evening,"
            else -> "Good Night,"
        }

        tvGreet.text = greetingText
        tvName.text = profil.getString("name", null)

//      Profile Image
        Picasso.get().load(profil.getString("profile", null)).into(imgProfile)

//        imgProfile.setOnClickListener {
//            startActivity(Intent(this,UpdateProfile::class.java))
//        }
//      Date
        val today = Date()

        tvDate.text = formatDate(today)
//      Image Status


        if (present){
            imgStatus.setImageResource(R.drawable.done)
        }else{
            imgStatus.setImageResource(R.drawable.info)
        }
//      Attendance Status
        if (present){
            tvStatus.text = "Anda Sudah Presensi"
        } else{
            tvStatus.text = "Anda Belum Presensi"
        }
//      ViewAll
        btnView.setOnClickListener {
            startActivity(Intent(this,AttendanceLog::class.java))
        }
//      Test Button Logout

        btnLogout.setOnClickListener {
            profil.edit().clear().apply()

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
        val formatter = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()) // EEE for day of week (e.g., Mon)
        return formatter.format(date)
    }
}

private fun Button.setOnClickListener(homePage: HomePage) {

}
