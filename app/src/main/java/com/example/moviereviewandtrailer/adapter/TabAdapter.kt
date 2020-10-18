package com.example.moviereviewandtrailer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.moviereviewandtrailer.fragment.NowPlayingFragment
import com.example.moviereviewandtrailer.fragment.PopularFragment

class TabAdapter(fragmentManager: FragmentManager, lifecycle:Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    val fragments : ArrayList<Fragment> = arrayListOf(
        PopularFragment(),
        NowPlayingFragment()
    )

    override fun getItemCount(): Int {
        return  fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}