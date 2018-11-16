package com.example.oscar.facebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import com.example.oscar.dummy.R
import com.xwray.groupie.Section


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val adapter = GroupAdapter<ViewHolder>()
        rvToDoList.adapter = adapter

        val section = Section()
        section.setHeader(HeaderItem())

        adapter.add(section)
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())



        // Get the ViewPager and set it's PagerAdapter so that it can display items
        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        viewPager.adapter = PageAdapter(
            supportFragmentManager,
            this@MainActivity
        )

        // Give the TabLayout the ViewPager
        val tabLayout = findViewById<View>(R.id.sliding_tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)

    }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true
    }
}
