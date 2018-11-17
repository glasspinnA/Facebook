package com.example.oscar.facebook

class StatusText(val userId: String, val statusMessage: String, val timestamp: Long, val nbrLikes: Int, val nbrCommets: Int){
    constructor(): this("","",-1, -1, -1)
}