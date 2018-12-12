package com.example.oscar.facebook


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.oscar.dummy.R
import com.example.oscar.facebook.FeedFragment.Companion.USER_KEY
import com.google.firebase.database.*
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.custom_post_row.view.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.header_row.view.*
import com.example.oscar.facebook.MainActivity.Companion.currentLogInUser
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap


/**
 * A simple [Fragment] subclass.
 */
class FeedFragment : Fragment() {

    private var root: View? = null   // create a global variable which will hold your layout
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private val hashMapStatusTexts = LinkedHashMap<String,StatusText>()

    companion object {
        private var testu: User? = null
        val USER_KEY = "USER_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_feed, container, false)
        //fetchStatusTextFromDB()
        fetchCurrentUser()
        root!!.rvToDo.adapter = groupAdapter
        return root
    }



    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref  = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                testu = p0.getValue(User::class.java)
                fetchStatusTextFromDB()
                //updateRecyclerView()
            }
        })
    }

    private fun fetchStatusTextFromDB() {
        val ref = FirebaseDatabase.getInstance().getReference("status")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val value = p0.getValue(StatusText::class.java) ?: return
                hashMapStatusTexts[p0.key!!] = value
                updateRecyclerView()
            }
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    private fun updateRecyclerView(){
        groupAdapter.clear()
        if(testu == null){
            fetchCurrentUser()
            //Log.d("Feed", testu!!.userId)
            groupAdapter.add(HeaderItem(testu!!))
        }else{
            groupAdapter.add(HeaderItem(testu!!))
        }

        for(i in hashMapStatusTexts.values.reversed()){
            groupAdapter.add(UserItem(context!!,i))
        }

    }
}
