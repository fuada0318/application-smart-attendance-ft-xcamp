package com.pnj.pbl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pnj.pbl.R
import com.pnj.pbl.data.ResponseAttendanceLog

class HomeLogAdapter (val arrayAtt:ArrayList<ResponseAttendanceLog.DataAtt>):RecyclerView.Adapter<HomeLogAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txDate = itemView.findViewById<TextView>(R.id.txtDate)
        val txHour = itemView.findViewById<TextView>(R.id.txtHour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_attendance,parent,false))
    }

    override fun getItemCount(): Int = arrayAtt.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemList = arrayAtt[position]

        val dateTimeParts = itemList.timestamp.split(" ")
        val date = dateTimeParts.take(4).joinToString(" ")
        val time = dateTimeParts.last()

        holder.apply {

            txDate.text = date
            txHour.text = time
        }

    }

}