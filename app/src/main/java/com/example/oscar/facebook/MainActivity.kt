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
import android.widget.TableLayout
import com.example.oscar.dummy.R
import com.xwray.groupie.Section


class MainActivity : AppCompatActivity() {
    private val tabIcons = intArrayOf(
        R.drawable.ic_outline_featured_play_list_24px,
        R.drawable.ic_outline_people_24px,
        R.drawable.ic_outline_notifications_24px)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)
        tabs_main.getTabAt(0)?.setIcon(tabIcons[0])
        tabs_main.getTabAt(1)?.setIcon(tabIcons[1])
        tabs_main.getTabAt(2)?.setIcon(tabIcons[2])
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        searchView.setIconified(false)
        searchView.clearFocus()
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true
    }

}
