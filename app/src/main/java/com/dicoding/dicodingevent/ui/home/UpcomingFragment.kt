package com.dicoding.dicodingevent.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.core.ui.EventAdapter
import com.dicoding.dicodingevent.core.util.DataHelper
import com.dicoding.dicodingevent.core.data.remote.network.ApiResponse
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpcomingFragment : Fragment(R.layout.fragment_upcoming) {
    private val binding by viewBinding(FragmentUpcomingBinding::bind)
    private val upcomingViewModel: UpcomingViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewAdapter()
        getCompletedEvents()
    }

    private fun setupRecyclerViewAdapter() {
        eventAdapter = EventAdapter(object : EventAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                DataHelper.eventId = id
                findNavController().navigate(R.id.action_home_fragment_to_detail_fragment)
            }
        })
        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcoming.adapter = eventAdapter
    }

    private fun getCompletedEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                upcomingViewModel.upcomingEvents.collectLatest { state ->
                    when (state) {
                        is ApiResponse.Loading -> setUiVisibility(View.VISIBLE, View.GONE, View.GONE)
                        is ApiResponse.Success -> {
                            setUiVisibility(View.GONE, View.VISIBLE, View.GONE)
                            eventAdapter.submitList(state.data)
                        }
                        is ApiResponse.Empty -> {
                            setUiVisibility(View.GONE, View.GONE, View.VISIBLE)
                            binding.tvMessage.text = getString(R.string.empty_upcoming_event)
                        }
                        is ApiResponse.Error -> {
                            setUiVisibility(View.GONE, View.GONE, View.VISIBLE)
                            binding.tvMessage.text = getString(R.string.no_internet_connection)
                        }
                    }
                }
            }
        }
    }

    private fun setUiVisibility(pbVisibility: Int, rvVisibility: Int, tvVisibility: Int) {
        binding.pbUpcoming.visibility = pbVisibility
        binding.rvUpcoming.visibility = rvVisibility
        binding.tvMessage.visibility = tvVisibility
    }
}