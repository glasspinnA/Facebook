package com.example.oscar.facebook

class StatusComment(val profileName: String, val profilePicUrl: String, val commentText: String, val timestamp: Long) {
    constructor():this("","","",-1)
}
