package com.example.oscar.facebook

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            /*
            0 -> {
                FirstFragment()
            }
            1 -> SecondFragment()
            else -> {
                return ThirdFragment()
            }
            */
            0 -> {
                FeedFragment()
            }
            1 -> {
                FriendsFragment()
            }
            else -> {
                return FriendsFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Feed"
            1 -> "Friends"
            else -> {
                return "Notifications"
            }
        }
    }
}