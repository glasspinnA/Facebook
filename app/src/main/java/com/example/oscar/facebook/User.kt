package com.example.oscar.facebook

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val userId: String, val firstname: String, val profilePhotoUrl: String): Parcelable{
    constructor() : this("","","")
}