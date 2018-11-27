package com.example.oscar.facebook

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import com.example.oscar.dummy.R
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.custom_post_row.view.*

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
                val nbrLikes = (statusTextObj.nbrLikes + 1)

                updateLikeCounter(statusTextObj.postId, nbrLikes)

                holder.tvLikes.text = nbrLikes.toString()
            }

            holder.custom_post_btn_comment.setOnClickListener {
                context as MainActivity
                context.fetchStatusComments(statusTextObj.postId)
                context.showCommentPanel()
            }

        }

    /**
     * Method that updates the number of likes on a status post
     */
    private fun updateLikeCounter(postId: String, nbrLikes: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("status/$postId").child("nbrLikes").setValue(nbrLikes)
    }

    /**
     * Method that convert timestamp to showing time as "time ago" on status posts
     */
    private fun timeConverter(timestamp: Long): CharSequence? {
            val timeAgo = DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            ).toString()
            return timeAgo
        }
    }