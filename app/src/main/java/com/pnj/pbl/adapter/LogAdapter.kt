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

class LogAdapter(val arrayLog:ArrayList<ResponseAttendanceFiltered.DataAtt>): RecyclerView.Adapter<LogAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txDate = itemView.findViewById<TextView>(R.id.txtDate2)
        val txHour = itemView.findViewById<TextView>(R.id.txtHour2)
        val txArrow = itemView.findViewById<TextView>(R.id.txtArrow)
        val lineTop = itemView.findViewById<View>(R.id.topLine)
        val lineBot = itemView.findViewById<View>(R.id.bottomLine)
        val imgCaptured = itemView.findViewById<ImageView>(R.id.captFace)
        val layout = itemView.findViewById<ConstraintLayout>(R.id.layout_main)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_attendance_filtered,parent,false))
    }

    override fun getItemCount(): Int = arrayLog.size

    fun getItemLate(logItems: List<ResponseAttendanceFiltered.DataAtt>): Int {
        val lateTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse("09:30")
        var lateCount = 0

        for (item in logItems) {
            val timePart = item.timestamp.split(" ").last()
            val clockInTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(timePart)

            if (clockInTime.after(lateTime)) {
                lateCount++
            }
        }

        return lateCount
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemList = arrayLog[position]

        val dateTimeParts = itemList.timestamp.split(" ")
        val date = dateTimeParts.take(4).joinToString(" ")
        val time = dateTimeParts.last()

        val lateTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse("08:30")
        var lateCount = 0

        for (item in time) {

            val clockInTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(time)

            if (clockInTime.after(lateTime)) {
                lateCount++
            }
        }

        holder.apply {
            txDate.text = date
            txHour.text = time
            Picasso.get().load(itemList.captured_face_url).into(imgCaptured)
        }

        val isExpandable : Boolean = itemList.isExtended
        if (isExpandable){
            holder.imgCaptured.visibility = View.VISIBLE
            holder.txArrow.text = "▲"
            holder.lineBot.visibility = View.VISIBLE
            holder.lineTop.visibility = View.GONE
        } else {
            holder.imgCaptured.visibility = View.GONE
            holder.txArrow.text = "▼"
            holder.lineBot.visibility = View.GONE
            holder.lineTop.visibility = View.VISIBLE
        }

        holder.layout.setOnClickListener {
            itemList.isExtended = !itemList.isExtended
            notifyItemChanged(position)
        }

    }
}