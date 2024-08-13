package com.pnj.pbl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pnj.pbl.R
import com.pnj.pbl.data.ResponseAttendanceFiltered
import com.pnj.pbl.data.ResponseAttendanceLog
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class ConfirmAdapter(val arrayLog:ArrayList<ResponseAttendanceLog.DataAtt>): RecyclerView.Adapter<ConfirmAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val txHour = itemView.findViewById<TextView>(R.id.txtHour2)
        val imgCaptured = itemView.findViewById<ImageView>(R.id.captFace)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_attendance_confirm,parent,false))
    }

    override fun getItemCount(): Int = arrayLog.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemList = arrayLog[position]

        val dateTimeParts = itemList.timestamp.split(" ")
        val date = dateTimeParts.take(4).joinToString(" ")
        val time = dateTimeParts.last()


        holder.apply {
            txHour.text = "Apakah anda melakukan presensi pada $time?"
            Picasso.get().load(itemList.captured_face_url).into(imgCaptured)
        }

    }
}