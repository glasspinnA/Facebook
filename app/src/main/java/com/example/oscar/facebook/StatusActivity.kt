package com.example.oscar.facebook

import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.example.oscar.dummy.R
import android.view.MenuItem
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_status.*
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import android.provider.MediaStore.Images
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class StatusActivity : AppCompatActivity() {
    private val TAG = "StatusActivity"
    private var currentUser: User? = null
    private var imagePath: Uri? = null
    private var  stringImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        currentUser = intent.getParcelableExtra<User>(FeedFragment.USER_KEY)
        supportActionBar?.title = "Create a post"

        Log.d(TAG,currentUser.toString())

        retriveImageForPost()
    }

    private fun retriveImageForPost(){
        if(intent.hasExtra("bitmap")){
            val b = BitmapFactory.decodeByteArray(intent.getByteArrayExtra("bitmap"),
                0, intent.getByteArrayExtra("bitmap").size
            )
            val bitmapDrawable = BitmapDrawable(b)
            //activity_status_iw_image.setImageDrawable(bitmapDrawable)

            val path = Images.Media.insertImage(this.getContentResolver(), b, "Title", null)
            imagePath = Uri.parse(path)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.getItemId()
        if(id == R.id.btnCreatePost){

            if(imagePath == null){
                createPost()
            }else{
                Log.d(TAG,"GOES THIS WAY")
                uploadProfilePicToStorage()
            }
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


        if(stringImage != null){
            Log.d(TAG,"String Image is not Null")
            val statusTextObject = StatusText(ref.key!!,userId!!,userPhoto!!,userName!!,text,timestamp, nbrLikes, nbrCommets, "-0-")
            ref.setValue(statusTextObject)
        }else if(stringImage == null){
            val statusTextObject = StatusText(ref.key!!,userId!!,userPhoto!!,userName!!,text,timestamp, nbrLikes, nbrCommets, "-0-")
            Log.d(TAG,"String Image is null")
            ref.setValue(statusTextObject)
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun uploadProfilePicToStorage() {
        if(imagePath == null ) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(imagePath!!)
            .addOnSuccessListener {
                Log.d(TAG, "Succesfully uplodaded photo ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File located $it")
                    fetchCurrentUser(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Didnt upload photo")
            }
    }

    private fun fetchCurrentUser(toString: String) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val currentLogInUser = p0.getValue(User::class.java)
                //Log.d("MainActivity", "Current user ${MainActivity.currentLogInUser?.profilePhotoUrl}" )
                testCreatePost(toString,currentLogInUser)
            }
        })
    }

    private fun testCreatePost(stringImage: String, currentLogInUser: User?) {


        Log.d(TAG,currentLogInUser.toString())


        val ref = FirebaseDatabase.getInstance().getReference("status").push()

        val userId = currentLogInUser?.userId
        val userPhoto = currentLogInUser?.profilePhotoUrl
        val userName = currentLogInUser?.firstname
        val text = etCreateStatus.text.toString()
        val timestamp = System.currentTimeMillis()
        val nbrLikes = -1
        val nbrCommets = -1


        val statusTextObject = StatusText(ref.key!!,userId!!,userPhoto!!,userName!!,text,timestamp, nbrLikes, nbrCommets, stringImage)
            ref.setValue(statusTextObject)
                .addOnSuccessListener {
                    Log.d(TAG, "Finally saved user to DB")
                }

                .addOnFailureListener {
                    Log.d(TAG, "Didn't saved user to DB $it")
                }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_post_menu, menu)
        return true
    }
}
