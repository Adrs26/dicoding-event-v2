package com.dicoding.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.ui.EventAdapter
import com.dicoding.dicodingevent.di.FavoriteModuleDependencies
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.dicoding.favorite.databinding.ActivityFavoriteBinding
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

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
        setupViewBinding()
        setupRecyclerViewAdapter()
        getFavoriteEvents()
    }

    private fun setupViewBinding() {
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupRecyclerViewAdapter() {
        eventAdapter = EventAdapter(object : EventAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                    .apply {
                        putExtras(Bundle().apply {
                            putInt(DetailActivity.EXTRA_ID, id)
                        })
                    }
                startActivity(intent)
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