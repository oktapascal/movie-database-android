package com.example.moviereviewandtrailer.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviereviewandtrailer.R
import com.example.moviereviewandtrailer.adapter.TabAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tabadapter = TabAdapter(supportFragmentManager, lifecycle)
        view_pager.adapter = tabadapter

        val tabTitles = arrayOf("Popular", "Now Playing")
        TabLayoutMediator(tab_layout, view_pager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}