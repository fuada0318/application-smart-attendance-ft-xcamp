package com.pnj.pbl.data

data class ResponseAttendanceLog(
    var message : String,
    var operation_status : Int,
    var data :List<DataAtt>
) {
    data class DataAtt(
        val captured_face_url: String,
        val floor: Int,
        val name: String,
        val status: String,
        val timestamp: String,
        val user_id: Long
    )
}
