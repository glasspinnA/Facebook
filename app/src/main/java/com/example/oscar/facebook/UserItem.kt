package com.example.oscar.facebook

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.oscar.dummy.R
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.custom_post_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class UserItem(val context: Context, val statusTextObj: StatusText): Item<ViewHolder>() {
        var TAG = "UserItem"


        override fun getLayout(): Int {
            return R.layout.custom_post_row
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            val holder = viewHolder.itemView
            holder.tvProfileName.text = statusTextObj.firstname
            holder.tvStatusText.text = statusTextObj.statusMessage
            holder.custom_post_tv_time.text = timeConverter(statusTextObj.timestamp)
            holder.tvLikes.text = statusTextObj.nbrLikes.toString()
            holder.tvComments.text = statusTextObj.nbrCommets.toString()
            val picUrl = statusTextObj.userPhoto
            val targetImageView = holder.ivProfilePic
            Picasso.get().load(picUrl).into(targetImageView)
            Log.d(TAG,"PostID: " + statusTextObj.postId)


            holder.custom_post_btn_like.setOnClickListener {
                //postStatusComment(statusTextObj.postId)
            }

            holder.custom_post_btn_comment.setOnClickListener {
                (context as MainActivity).fetchStatusComments(statusTextObj.postId)
                (context as MainActivity).showCommentPanel()

            }

        }

        private fun postStatusComment(postId: String) {
            val ref = FirebaseDatabase.getInstance().getReference("status-comment/$postId").push()
            val dummyString: String = "Dis is a dummy comment"
            ref.setValue(StatusComment(dummyString))
                .addOnSuccessListener {
                    Log.d(TAG,"Comment successfully posted")
                }
                .addOnFailureListener{
                    Toast.makeText(context,"Comment could not be posted!", Toast.LENGTH_SHORT).show()
                }
        }

        private fun timeConverter(timestamp: Long): CharSequence? {
            val date = Date(timestamp * 1000L)
            val sdf = SimpleDateFormat("HH:mm")
            val formattedDate = sdf.format(date)

            return formattedDate
        }
    }