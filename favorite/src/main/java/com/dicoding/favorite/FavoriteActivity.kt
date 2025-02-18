package com.dicoding.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dicoding.core.ui.EventAdapter
import com.dicoding.core.util.DataHelper
import com.dicoding.dicodingevent.di.FavoriteModuleDependencies
import com.dicoding.dicodingevent.ui.main.MainActivity
import com.dicoding.favorite.databinding.ActivityFavoriteBinding
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity(R.layout.activity_favorite) {
    private val binding by viewBinding(ActivityFavoriteBinding::bind)

    @Inject
    lateinit var factory: FavoriteViewModelFactory
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        factory
    }

    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerFavoriteComponent.builder()
            .context(this)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
        setupRecyclerViewAdapter()
        getFavoriteEvents()
    }

    private fun setupRecyclerViewAdapter() {
        eventAdapter = EventAdapter(object : EventAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                DataHelper.eventId = id
                DataHelper.menuId = 1
                startActivity(Intent(this@FavoriteActivity, MainActivity::class.java))
            }
        })
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = eventAdapter
    }

    private fun getFavoriteEvents() {
        favoriteViewModel.favoriteEvents.observe(this) { events ->
            if (events.isNotEmpty()) {
                eventAdapter.submitList(events)
                binding.rvFavorite.visibility = View.VISIBLE
                binding.tvMessage.visibility = View.GONE
            } else {
                binding.rvFavorite.visibility = View.GONE
                binding.tvMessage.visibility = View.VISIBLE
                binding.tvMessage.text = getString(R.string.empty_favorite_event)
            }
        }
    }
}