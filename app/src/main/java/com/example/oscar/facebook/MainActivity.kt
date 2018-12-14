package com.example.oscar.facebook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.example.oscar.dummy.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.LinearLayout
import android.support.design.widget.BottomSheetBehavior
import android.widget.Toast
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet_camera_option.*
import java.util.*
import android.R.attr.data
import android.graphics.Bitmap
import android.R.attr.data
import android.R.attr.bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File.separator
import java.nio.file.Files.exists
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import java.io.File
import java.text.SimpleDateFormat
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.StrictMode
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    //TODO: implement Comment counter
    //TODO: implement like counter
    //TODO: FiX friends Fragment
    //TODO: Be able to post with camera / photos

    private val groupAdapter = GroupAdapter<ViewHolder>()
    private val hashMapStatusTexts = LinkedHashMap<String,StatusComment>()
    private var camera: Camera? = null


    private val tabIcons = intArrayOf(
        R.drawable.ic_outline_featured_play_list_24px,
        R.drawable.ic_outline_people_24px,
        R.drawable.ic_outline_notifications_24px)

    companion object {
        var currentLogInUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val fragmentAdapter = PagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        bottom_sheet_rv.adapter = groupAdapter

        initTabLayout()
        initCameraFunctions()

        startCameraPanel()

        logOutUser()
        fetchCurrentUser()
        verifyUser()

        checkPermissions()
    }

    /**
     * Method that contains button listener to start camera panel
     */
    private fun startCameraPanel(){
        btnPhoto.setOnClickListener{
            showCameraPanel()
        }
    }

    /**
     * Method for checking camera permissions
     */
    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }
    }

    /**
     * Method that inits the camera class
     */
    private fun initCameraFunctions() {
        camera = Camera(this)
    }

    /**
     * Method that inits the tab layout
     */
    private fun initTabLayout() {
        tabs_main.setupWithViewPager(viewpager_main)
        tabs_main.getTabAt(0)?.setIcon(tabIcons[0])
        tabs_main.getTabAt(1)?.setIcon(tabIcons[1])
        tabs_main.getTabAt(2)?.setIcon(tabIcons[2])
    }

    /**
     * Method that logs out the user from the app
     */
    private fun logOutUser() {
        main_btn_log_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    /**
     *  Method for fetching the current user logged in, in the app
     */
    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                currentLogInUser = p0.getValue(User::class.java)
                Log.d("MainActivity", "Current user ${currentLogInUser?.profilePhotoUrl}" )
            }
        })
    }

    /**
     * Method for verifying the logged in user
     */
    private fun verifyUser() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this,RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        searchView.isIconified = false
        searchView.clearFocus()
        return true
    }

    /**
     * Method for show camera panel
     */
    private fun showCameraPanel() {
        val llBottomSheetCamera = findViewById<LinearLayout>(R.id.bottom_sheet_camera_option)
        val bottomSheetCameraBehavior = BottomSheetBehavior.from(llBottomSheetCamera)

        if(bottomSheetCameraBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetCameraBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }else if(bottomSheetCameraBehavior.state == BottomSheetBehavior.STATE_COLLAPSED){
            bottomSheetCameraBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        cameraListeners()
    }

    /**
     * Method for button listener in camera panel
     */
    private fun cameraListeners(){
        btn_bottom_sheet_camera_camera.setOnClickListener{
            //dispatchTakePictureIntent(this)
           takePicture()
        }

        btn_bottom_sheet_camera_gallery.setOnClickListener{
            //showImageGallery()
        }
    }

/*
    fun takePicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(intent.resolveActivity(packageManager) != null){
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE)
        }
    }
    */

    private var output: File? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val SHOW_IMAGE_GALLERY = 0

        if(resultCode == Activity.RESULT_OK && data != null){
            if(requestCode == SHOW_IMAGE_GALLERY ){
                val selectedPhotoUri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

                val bs = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bs)

                val intent = Intent(this,StatusActivity::class.java)
                intent.putExtra("bitmap",bs.toByteArray())
                startActivity(intent)
            }

            if (requestCode == 1000) {
                if (resultCode == Activity.RESULT_OK) {
                    val i = Intent(Intent.ACTION_VIEW)

                    i.setDataAndType(Uri.fromFile(output), "image/jpeg")
                    startActivity(i)
                    finish()
                }
            }
        }
    }

    private fun takePicture() {

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build());

        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        Log.d("TAG",dir.toString())

        output = File(dir, "CameraContentDemo.jpeg")

        i.putExtra(MediaStore.EXTRA_OUTPUT, output)

        startActivityForResult(i, 1000)
    }

    private fun getOutputMediaFile(): File? {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Test")

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File(
            mediaStorageDir.getPath() + File.separator +
            "IMG_" + timeStamp + ".jpg"
        )
}


    /**
     * Method for showing comment panel
     */
    fun showCommentPanel() {
        clearList()
        val llBottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    /**
     * Method for fetching comments on a given status
     */
    fun fetchStatusComments(postId: String, nbrCommets: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("status-comment/$postId")

        listenForPostCommentText(postId, nbrCommets)

        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val response = p0.getValue(StatusComment::class.java)?: return
                Log.d("MainActivity", "Key is: " + p0.key)
                hashMapStatusTexts[p0.key!!] = response
                //updateRView()
                Log.d("MainActivity", "Comment is : ${response.commentText}")
            }
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    /**
     * Method that listen if the users is about to send away a comment on a status
     */
    private fun listenForPostCommentText(postId: String, nbrCommets: Int) {
        bottom_sheet_btn_post.setOnClickListener {
            val commentText = bottom_sheet_et_comment.text.toString()
            if(commentText.isEmpty()){
                Toast.makeText(this,"Enter a comment!",Toast.LENGTH_SHORT).show()
            }else{
                postStatusCommentToDB(postId, commentText, nbrCommets)
            }
        }
    }

    /**
     * Method that storage comment on a given post on Firebase
     */
    private fun postStatusCommentToDB(postId: String, commentText: String, nbrCommets: Int) {
        val updateNbrComments = nbrCommets + 1
        val currentTimestamp = System.currentTimeMillis()
        val ref = FirebaseDatabase.getInstance().getReference("status-comment/$postId").push()
        ref.setValue(StatusComment(currentLogInUser!!.firstname, currentLogInUser!!.profilePhotoUrl,commentText,currentTimestamp))
            .addOnSuccessListener {
                Log.d("MainActivity","Comment successfully posted")
                val ref = FirebaseDatabase.getInstance().getReference("status/$postId").child("nbrCommets").setValue(updateNbrComments)
                bottom_sheet_et_comment.text.clear()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Comment could not be posted!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateRView(){
        groupAdapter.clear()
        for(i in hashMapStatusTexts.values.reversed()){
            groupAdapter.add(CommentItem(i))
        }
    }

    /**
     * Method for clearing list
     */
    private fun clearList(){
        groupAdapter.clear()
        hashMapStatusTexts.clear()
    }

    /**
     * Passage method from headerItem to camera class
     */
    fun createImagePostPassage(){
        camera?.showImageGallery()
    }
}

