package com.example.oscar.facebook


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.oscar.dummy.R
import com.google.firebase.database.*
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.custom_post_row.view.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.header_row.view.*
import com.example.oscar.facebook.MainActivity.Companion.currentUser
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


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
        fetchCurrentUser()
        fetchStatusTextFromDB()
        root!!.rvToDo.adapter = adapter

        return root
    }


    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                adapter.add(HeaderItem(currentUser!!))
                Log.d("MainActivity", "Current user ${currentUser?.profilePhotoUrl}" )
            }
        })
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

class HeaderItem(val user: User) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.header_row
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {


        Log.d("FEED", user.profilePhotoUrl)
        val picUrl = user.profilePhotoUrl
        val targetImageView = viewHolder.itemView.header_row_iw_profile
        Picasso.get().load(picUrl).into(targetImageView)
        /*
        viewHolder.itemView.header_row_create_post.setOnClickListener {
            val coustomContext = it.context
            val i = Intent(coustomContext,StatusActivity::class.java)
            coustomContext.startActivity(i)
        }
        */
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
