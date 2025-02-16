package com.dicoding.dicodingevent.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.core.ui.EventAdapter
import com.dicoding.dicodingevent.core.util.DataHelper
import com.dicoding.dicodingevent.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private val binding by viewBinding(FragmentFavoriteBinding::bind)
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackButton()
        setupRecyclerViewAdapter()
        getFavoriteEvents()
    }

    private fun setupBackButton() {
        binding.ibBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerViewAdapter() {
        eventAdapter = EventAdapter(object : EventAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                DataHelper.eventId = id
                findNavController().navigate(R.id.action_favorite_fragment_to_detail_fragment)
            }
        })
        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.adapter = eventAdapter
    }

    private fun getFavoriteEvents() {
        favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
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