package com.dicoding.dicodingevent.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.dicoding.core.util.DataHelper
import com.dicoding.dicodingevent.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (DataHelper.menuId == 1) {
            findNavController(R.id.nav_host_main)
                .navigate(R.id.action_home_fragment_to_detail_fragment)
        }
    }
}