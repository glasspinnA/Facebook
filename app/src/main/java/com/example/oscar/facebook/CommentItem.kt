package com.example.oscar.facebook

import android.util.Log
import com.example.oscar.dummy.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.comment_item_row.view.*
import kotlinx.android.synthetic.main.custom_post_row.view.*

class CommentItem(val response: StatusComment) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.comment_item_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.comment_item_tv_profile_name.text = response.profileName
        viewHolder.itemView.comment_item_tv_status.text = response.commentText
        val picUrl = response.profilePicUrl
        val targetImageView = viewHolder.itemView.comment_item_iv_profile_pic
        Picasso.get().load(picUrl).into(targetImageView)

        Log.d("CommentItem", "NULL " + response.commentText)
    }

}
