package com.example.oscar.facebook

import android.util.Log
import com.example.oscar.dummy.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.comment_item_row.view.*

class CommentItem(val response: StatusComment) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.comment_item_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.comment_item_tv_status.text = response.commentText
        Log.d("CommentItem", "NULL " + response.commentText)
    }

}
