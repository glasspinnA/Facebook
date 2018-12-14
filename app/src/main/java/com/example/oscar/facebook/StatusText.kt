package com.example.oscar.facebook

class StatusText(val postId: String,val userId: String, val userPhoto: String, val firstname: String, val statusMessage: String, val timestamp: Long, val nbrLikes: Int, val nbrCommets: Int, val statusPhoto: String){
    constructor(): this("","","","","",-1,-1, -1,"")
}