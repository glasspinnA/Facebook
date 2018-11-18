package com.example.oscar.facebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.oscar.dummy.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            userLogin()
        }

        already_have_an_account_login.setOnClickListener {
            finish()
        }
    }

    private fun userLogin() {
        val password = password_login_input.text.toString()
        val email = email_login_input.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    Log.d("LoginActivity", "User logged in successfully")
                }
                .addOnFailureListener {
                    Log.d("LoginActivity", "User failed to login")
                }
    }
}