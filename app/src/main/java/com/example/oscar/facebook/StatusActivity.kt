package com.example.oscar.facebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import com.example.oscar.dummy.R
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_status.*


class StatusActivity : AppCompatActivity() {

    private val TAG = "StatusActivity"
    var currentUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        currentUser = intent.getParcelableExtra<User>(FeedFragment.USER_KEY)
        supportActionBar?.title = "Create a post"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_post_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.getItemId()
        if(id == R.id.btnCreatePost){
            createPost()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createPost() {
        val ref = FirebaseDatabase.getInstance().getReference("status").push()
        val userId = currentUser?.userId
        val userPhoto = currentUser?.profilePhotoUrl
        val userName = currentUser?.firstname
        val text = etCreateStatus.text.toString()
        val timestamp = System.currentTimeMillis()
        val nbrLikes = -1
        val nbrCommets = -1
        val statusTextObject = StatusText(ref.key!!,userId!!,userPhoto!!,userName!!,text,timestamp, nbrLikes, nbrCommets)
        ref.setValue(statusTextObject)
    }

    override fun onBackPressed() {
        Log.d(TAG,"BACK PRESSED")
        finish()
    }
}
