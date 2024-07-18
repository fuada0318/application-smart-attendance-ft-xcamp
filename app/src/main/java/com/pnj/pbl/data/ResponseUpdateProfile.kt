package com.pnj.pbl.data

data class ResponseUpdateProfile(
    var message : String,
    var operation_status : Int,
    var data : pictURl
){
    data class pictURl(
        var profile_pict_url : String
    )
}
