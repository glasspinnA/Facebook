package com.example.oscar.facebook

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.oscar.dummy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var selectedPhotoUri: Uri? = null
    var mainString = "MAIN"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            performUserRegistration()
        }

        alreadyHaveAccountTextView.setOnClickListener{
            startLoginActivity()
        }

        select_photo_button_register.setOnClickListener {
            selectProfilePic()
        }
    }

    private fun selectProfilePic() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,0)
    }

    private fun startLoginActivity() {
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            select_photo_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }


    private fun performUserRegistration() {
        val email = email_input.text.toString()
        val password = password_input.text.toString()

        if(email.isEmpty() ||password.isEmpty()) {
            Toast.makeText(this, "Please enter a text in email and password", Toast.LENGTH_LONG).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                uploadProfilePicToStorage()
                Log.d("MAIN","Successfully created user with uid: ${it.result.user.uid} ")
            }
            .addOnFailureListener {
                Toast.makeText(this, "$it.message", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadProfilePicToStorage() {
        if(selectedPhotoUri == null ) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(mainString, "Succesfully uplodaded photo ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(mainString, "File located $it")
                    saveUserToDB(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(mainString, "Didnt upload photo")
            }
    }

    private fun saveUserToDB(profilePhotoUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_input.text.toString(), profilePhotoUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Log.d(mainString, "Finally saved user to DB")
            }

            .addOnFailureListener {
                Log.d(mainString, "Didn't saved user to DB")
            }
    }
}
