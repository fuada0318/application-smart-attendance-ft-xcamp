package com.pnj.pbl

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.health.connect.datatypes.units.Percentage
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pnj.pbl.api.RetrofitClient
import com.pnj.pbl.data.PrefManager
import com.pnj.pbl.data.RequestUpdateProfil
import com.pnj.pbl.data.ResponseUpdateProfile
import com.pnj.pbl.other.getFileName
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ProfilePage : AppCompatActivity(), RequestUpdateProfil.UploadCallback {
    private lateinit var imgProfil: CircleImageView
    private lateinit var btnLogout : Button
    private lateinit var btnUpdate : Button
    private lateinit var btnUpload : Button
    private lateinit var btnBack : Button
    private lateinit var txUserid : TextView
    private lateinit var txNama : TextView
    private lateinit var txEmail : TextView
    private lateinit var progBar : ProgressBar


    private var selectedImage : Uri? = null
    private lateinit var profil : PrefManager

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
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnUpdate = findViewById(R.id.btnProfile)
        btnUpload = findViewById(R.id.btnUpload)
        btnLogout = findViewById(R.id.btnLogout)
        btnBack = findViewById(R.id.btnBack2)
        imgProfil = findViewById(R.id.imgProfil)
        txUserid = findViewById(R.id.txUserid)
        txNama = findViewById(R.id.txNama)
        txEmail = findViewById(R.id.txEmail)
        progBar = findViewById(R.id.progBar)

        profil = PrefManager(this)
        val tokenJWT = "Bearer ${profil.getToken()}"
//      Fungsi back
        btnBack.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
//      Menampilkan Data
        Picasso.get().load(profil.getProfile()).into(imgProfil)
        txUserid.text= profil.getId().toString()
        txNama.text= profil.getName()
        txEmail.text= profil.getEmail()
//      Fungsi update profile
        btnUpdate.setOnClickListener {
//            Toast.makeText(this, "Fitur belum ada", Toast.LENGTH_SHORT).show()
            openImageChooser()
        }
        btnUpload.setOnClickListener {
            uploadImage(tokenJWT)
        }

//      Fungsi LogOut
        btnLogout.setOnClickListener {
            profil.logOut()

            val intent = Intent(this, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun uploadImage(token : String) {
        val parceFileDescriptor = contentResolver.openFileDescriptor(selectedImage!!,"r",null) ?: return
        val file = File(cacheDir,contentResolver.getFileName(selectedImage!!))
        val inputStream = FileInputStream(parceFileDescriptor.fileDescriptor)
        val ouputStream = FileOutputStream(file)
        inputStream.copyTo(ouputStream)
        progBar.visibility = View.VISIBLE

        val body = RequestUpdateProfil(file,"image", this)
        val api = RetrofitClient().getDataAPI()
        api.updatePP(token,MultipartBody.Part.createFormData("profile_pict", file.name,body)).enqueue(object: Callback<ResponseUpdateProfile>{
            override fun onResponse(
                call: Call<ResponseUpdateProfile>,
                response: Response<ResponseUpdateProfile>
            ) {
                progBar.progress = 100
                progBar.visibility = View.GONE
                showToast(response.body()?.message.toString())
                profil.setProfile(response.body()!!.data.profile_pict_url)
                Picasso.get().load(profil.getProfile()).into(imgProfil)
                btnUpload.visibility = View.GONE
                btnUpdate.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<ResponseUpdateProfile>, t: Throwable) {
                Log.e("Error Upload Foto", "${t.message}")
            }

        })
    }

    private fun openImageChooser(){
        Intent(Intent.ACTION_PICK).also{
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/jpg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_IMAGE_PICKER -> {
                    selectedImage = data?.data
                    imgProfil.setImageURI(selectedImage)
                    btnUpload.visibility = View.VISIBLE
                    btnUpdate.visibility = View.GONE
                }
            }
        } else{
            btnUpload.visibility = View.GONE
            btnUpdate.visibility = View.VISIBLE
        }
    }

    companion object{
        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    private fun showToast(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    override fun onProgressUpdate(percentage: Int) {
        progBar.progress = percentage
    }
}