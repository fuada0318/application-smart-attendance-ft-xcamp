package com.pnj.pbl.data

//data class LoginRequest(
//    val email : String,
//    val password : String
//)

data class LoginResponse(
    var message : String,
    var operation_status : Int,
    var data : UserData,
    var token : String
)
data class User(
    val email: String,
    val floor: Int,
    val name: String,
    val profile_pict_url: String,
    val role: Int,
    val user_id: Long
)
data class UserData(
    val user : User
)
