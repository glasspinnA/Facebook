package com.example.oscar.facebook

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


val REQUEST_IMAGE_CAPTURE = 1
val REQUEST_TAKE_PHOTO = 1

fun dispatchTakePictureIntent(mainActivity: MainActivity) {

    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        // Ensure that there's a camera activity to handle the intent
        takePictureIntent.resolveActivity(mainActivity.packageManager)?.also {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile(mainActivity)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    mainActivity,
                    "com.example.android.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                Log.d("TEST","TEST")
                mainActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }
}

var mCurrentPhotoPath: String = ""

@Throws(IOException::class)
private fun createImageFile(mainActivity: MainActivity): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = absolutePath
        galleryAddPic(mainActivity)
    }
}

private fun galleryAddPic(mainActivity: MainActivity) {
    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
        val f = File(mCurrentPhotoPath)
        mediaScanIntent.data = Uri.fromFile(f)
        mainActivity.sendBroadcast(mediaScanIntent)
    }
}
