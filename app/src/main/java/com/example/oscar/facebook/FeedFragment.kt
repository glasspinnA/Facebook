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
import com.google.firebase.database.*
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.custom_post_row.view.*
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*


/**
 * A simple [Fragment] subclass.
 */
class FeedFragment : Fragment() {

    private var root: View? = null   // create a global variable which will hold your layout
    private val adapter = GroupAdapter<ViewHolder>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_feed, container, false)



        val section = Section(HeaderItem())
        adapter.add(section)

        root!!.rvToDo.adapter = adapter

        makeStatusUpdate()
        fetchStatusTextFromDB()
        return root
    }


    private fun makeStatusUpdate() {
        val userId = "0"
        val text = "very new statustext"
        val timestamp = System.currentTimeMillis() / 1000
        val nbrLikes = -1
        val nbrCommets = -1
        val statusTextObject  = StatusText(userId,text,timestamp, nbrLikes, nbrCommets)

        val ref = FirebaseDatabase.getInstance().getReference("status").push()
        ref.setValue(statusTextObject)
    }


    private fun fetchStatusTextFromDB() {
        val ref = FirebaseDatabase.getInstance().getReference("status")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val value = p0.getValue(StatusText::class.java) ?: return
                adapter.add(UserItem(value))
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }


}


class UserItem(val statusTextObj: StatusText): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.custom_post_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvProfileName.text = statusTextObj.userId
        viewHolder.itemView.tvStatusText.text = statusTextObj.statusMessage
        viewHolder.itemView.tvLikes.text = statusTextObj.nbrLikes.toString()
        viewHolder.itemView.tvComments.text = statusTextObj.nbrCommets.toString()
    }
}

class HeaderItem: Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.header_row
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

}