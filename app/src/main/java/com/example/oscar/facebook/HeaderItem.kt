package com.example.oscar.facebook

import android.content.Intent
import android.view.View
import com.example.oscar.dummy.R
import com.example.oscar.facebook.FeedFragment.Companion.USER_KEY
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.header_row.view.*

class HeaderItem(val user: User, val mainActivity: MainActivity) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.header_row
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val picUrl = user.profilePhotoUrl
        val targetImageView = viewHolder.itemView.header_row_iw_profile
        Picasso.get().load(picUrl).into(targetImageView)

        viewHolder.itemView.header_row_create_post.setOnClickListener {
            createPost(it)
        }

        viewHolder.itemView.header_row_btn_select_photo.setOnClickListener{
            createImagePost()
        }
    }

    /**
     * Method for starting activity to create a post
     */
    private fun createPost(it: View) {
        val customContext = it.context
        val i = Intent(customContext,StatusActivity::class.java)
        i.putExtra(USER_KEY,user)
        customContext.startActivity(i)
    }

    /**
     * Method for starting activity to create a post with a image
     */
    private fun createImagePost(){
        mainActivity.createImagePostPassage()
    }

}

