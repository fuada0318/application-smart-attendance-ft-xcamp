package com.pnj.pbl.data

data class ResponseAttendanceLog(
    var message : String,
    var operation_status : Int,
    var data : DataData
)
data class Date(
    val captired_face_url: String,
    val floor: Int,
    val status: String,
    val timestamp: String,
)
data class DataData(
    val data : Date
)
