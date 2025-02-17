package com.dicoding.dicodingevent.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dicoding.dicodingevent.R
import com.dicoding.core.data.remote.network.ApiResponse
import com.dicoding.core.ui.EventAdapter
import com.dicoding.core.util.DataHelper
import com.dicoding.dicodingevent.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackButton()
        setupRecyclerViewAdapter()
        setupSearchView()
        setupSearchObserver()
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
                findNavController().navigate(R.id.action_search_fragment_to_detail_fragment)
            }
        })
        binding.rvSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearch.adapter = eventAdapter
    }

    private fun setupSearchView() {
        binding.apply {
            searchView.queryHint = getString(R.string.search_your_favorite_event)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean { return false }
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        searchViewModel.searchEvents(query)
                    }
                    return false
                }
            })
        }
    }

    private fun setupSearchObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchResults.collectLatest { state ->
                    when (state) {
                        is ApiResponse.Loading -> setUiVisibility(View.VISIBLE, View.GONE, View.GONE)
                        is ApiResponse.Success -> {
                            setUiVisibility(View.GONE, View.VISIBLE, View.GONE)
                            eventAdapter.submitList(state.data)
                        }
                        is ApiResponse.Empty -> {
                            setUiVisibility(View.GONE, View.GONE, View.VISIBLE)
                            binding.tvMessage.text = getString(R.string.event_not_found)
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
        binding.pbSearch.visibility = pbVisibility
        binding.rvSearch.visibility = rvVisibility
        binding.tvMessage.visibility = tvVisibility
    }
}