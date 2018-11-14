package com.example.oscar.facebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.*
import com.xwray.groupie.groupiex.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = GroupAdapter<ViewHolder>()
        rv.adapter = adapter

        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val mLayoutManager = LinearLayoutManager(applicationContext)
        rv.setLayoutManager(mLayoutManager)

        val section = Section()
        section.setHeader(HeaderItem())

        adapter.add(section)
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())

    }
    class UserItem: Item<ViewHolder>(){
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
}
