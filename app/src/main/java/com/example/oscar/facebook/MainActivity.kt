package com.example.oscar.facebook

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


class MainActivity : AppCompatActivity() {
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private val hashMapStatusTexts = LinkedHashMap<String,StatusComment>()

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


        initTabLayout()

        bottom_sheet_rv.adapter = groupAdapter


        logOutUser()

        fetchCurrentUser()
        verifyUser()
    }

    private fun initTabLayout() {
        tabs_main.setupWithViewPager(viewpager_main)
        tabs_main.getTabAt(0)?.setIcon(tabIcons[0])
        tabs_main.getTabAt(1)?.setIcon(tabIcons[1])
        tabs_main.getTabAt(2)?.setIcon(tabIcons[2])

    }

    private fun logOutUser() {
        main_btn_log_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                currentLogInUser = p0.getValue(User::class.java)
                Log.d("MainActivity", "Current user ${currentLogInUser?.profilePhotoUrl}" )
            }
        })
    }

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

    fun showCommentPanel() {
        val llBottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun fetchStatusComments(postId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("status-comment/$postId")

        listenForPostCommentText(postId)

        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val response = p0.getValue(StatusComment::class.java)?: return
                hashMapStatusTexts[p0.key!!] = response
                updateRView()
                Log.d("MainActivity", "Comment is : ${response.commentText}")
            }
            override fun onChildRemoved(p0: DataSnapshot) {}
        })

    }

    private fun listenForPostCommentText(postId: String) {
        bottom_sheet_btn_post.setOnClickListener {
            val commentText = bottom_sheet_et_comment.text.toString()
            if(commentText.isEmpty()){
                Toast.makeText(this,"Enter a comment!",Toast.LENGTH_SHORT).show()
            }else{
                postStatusCommentToDB(postId, commentText)
            }
        }
    }

    private fun postStatusCommentToDB(postId: String, commentText: String) {
        val ref = FirebaseDatabase.getInstance().getReference("status-comment/$postId").push()
        ref.setValue(StatusComment(currentLogInUser!!.firstname, currentLogInUser!!.profilePhotoUrl,commentText))
            .addOnSuccessListener {
                Log.d("MainActivity","Comment successfully posted")
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
}

