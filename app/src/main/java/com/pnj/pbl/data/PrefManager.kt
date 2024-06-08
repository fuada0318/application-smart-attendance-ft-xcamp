package com.pnj.pbl.data

import android.content.Context
import android.content.SharedPreferences

class PrefManager(var context : Context) {
    val PRIVATE_MODE = 0

    private val PREF_NAME = "login_session"
    private val IS_LOGIN = "is_login"

    var pref:SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor:SharedPreferences.Editor = pref.edit()

    fun setLogin(isLogin:Boolean){
        editor.putBoolean(IS_LOGIN,isLogin)
        editor.commit()
    }

    fun getLogin():Boolean{
        return pref.getBoolean(IS_LOGIN,false)
    }

    fun logOut(){
        editor.clear()
        editor.commit()
    }

    fun setEmail(email:String){
        editor.putString("email",email)
        editor.commit()
    }
    fun setName(name:String){
        editor.putString("name",name)
        editor.commit()
    }
    fun setId(id:Long){
        editor.putLong("user_id",id)
        editor.commit()
    }
    fun setProfile(profile:String){
        editor.putString("profile",profile)
        editor.commit()
    }
    fun setFloor(floor:Int){
        editor.putInt("floor",floor)
        editor.commit()
    }
    fun setToken(token:String){
        editor.putString("token",token)
        editor.commit()
    }

    fun getEmail():String?{
        return pref.getString("email","")
    }
    fun getName():String?{
        return pref.getString("name","")
    }
    fun getId():Long?{
        return pref.getLong("user_id",-1L)
    }
    fun getProfile():String?{
        return pref.getString("profile","")
    }
    fun getFloor():Int?{
        return pref.getInt("floor",-1)
    }
    fun getToken():String?{
        return pref.getString("token","")
    }

//    .putString("email", response.body()?.data?.user?.email)
//    .putInt("floor", response.body()?.data?.user?.floor!!)
//    .putString("name", response.body()?.data?.user?.name)
//    .putString("profile", response.body()?.data?.user?.profile_pict_url)
//    .putInt("role", response.body()?.data?.user?.role!!)
//    .putLong("id", response.body()?.data?.user?.user_id!!)
//    .putString("jwt_token", response.body()?.token)
}