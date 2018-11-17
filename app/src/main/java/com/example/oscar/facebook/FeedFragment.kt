package com.example.oscar.facebook


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.oscar.dummy.R
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*


/**
 * A simple [Fragment] subclass.
 */
class FeedFragment : Fragment() {

    private var root: View? = null   // create a global variable which will hold your layout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_feed, container, false)


        val adapter = GroupAdapter<ViewHolder>()

        val section = Section(HeaderItem())
        adapter.add(section)
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())
        root!!.rvToDo.adapter = adapter
        return root
    }




}// Required empty public constructor



class UserItem: Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.custom_post_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
    }
}

class HeaderItem: Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.header_row
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

}