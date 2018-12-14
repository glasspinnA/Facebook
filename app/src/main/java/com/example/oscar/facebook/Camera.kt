package com.example.oscar.facebook

import android.content.Intent

class Camera(mainActivity: MainActivity) {
    private val TAG = "CameraClass"
    private val activity: MainActivity = mainActivity
    private val SHOW_IMAGE_GALLERY = 0

    /**
     * Method for selecting a image to post with a status
     */
    fun showImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent,SHOW_IMAGE_GALLERY)
    }
}