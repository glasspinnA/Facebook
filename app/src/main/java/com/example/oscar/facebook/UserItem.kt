package com.example.oscar.facebook

import android.util.Log
import com.example.oscar.dummy.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.custom_post_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class UserItem(val statusTextObj: StatusText): Item<ViewHolder>() {

    var TAG = "UserItem"


    override fun getLayout(): Int {
        return R.layout.custom_post_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvProfileName.text = statusTextObj.userName
        viewHolder.itemView.tvStatusText.text = statusTextObj.statusMessage
        viewHolder.itemView.custom_post_tv_time.text = timeConverter(statusTextObj.timestamp)
        viewHolder.itemView.tvLikes.text = statusTextObj.nbrLikes.toString()
        viewHolder.itemView.tvComments.text = statusTextObj.nbrCommets.toString()
        val picUrl = statusTextObj.userPhoto
        val targetImageView = viewHolder.itemView.ivProfilePic
        Picasso.get().load(picUrl).into(targetImageView)
        Log.d(TAG,"PostID: " + statusTextObj.postId)
        viewHolder.itemView.custom_post_btn_like.setOnClickListener {
            Log.d(TAG,"Button truckt")
            postStatusComment(statusTextObj.postId)
        }

        viewHolder.itemView.custom_post_btn_comment.setOnClickListener {
            fetchStatusComments(statusTextObj.postId)
        }

    }

    private fun fetchStatusComments(postId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("status-comment/$postId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val response = p0.getValue(StatusComment::class.java)?: return
                Log.d(TAG, "Comment is : ${response.comment}")
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })
    }

    private fun postStatusComment(postId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("status-comment/$postId").push()
        val dummyString: String = "Dis is a dummy comment"
        ref.setValue(StatusComment(dummyString))
            .addOnSuccessListener {
                Log.d(TAG,"Comment successfully posted")
            }
            .addOnFailureListener{
                Log.d(TAG,"Comment failed to post")
            }
    }

    private fun timeConverter(timestamp: Long): CharSequence? {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("HH:mm")
        val dateStr = dateFormat.format(date)
        return dateStr
    }
}