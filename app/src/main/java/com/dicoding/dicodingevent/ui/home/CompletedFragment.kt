package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.data.remote.network.ApiResponse
import com.dicoding.core.ui.EventAdapter
import com.dicoding.core.util.DataHelper
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.FragmentCompletedBinding
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompletedFragment : Fragment() {
    private var _binding: FragmentCompletedBinding? = null
    private val binding get() = _binding!!

    private val completedViewModel: CompletedViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewAdapter()
        getCompletedEvents()
    }

    private fun setupRecyclerViewAdapter() {
        eventAdapter = EventAdapter(object : EventAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putInt(DetailActivity.EXTRA_ID, id)
                    })
                }
                startActivity(intent)
            }
        })
        binding.rvCompleted.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompleted.adapter = eventAdapter
    }

    private fun getCompletedEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                completedViewModel.completedEvents.collectLatest { state ->
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
        binding.pbCompleted.visibility = pbVisibility
        binding.rvCompleted.visibility = rvVisibility
        binding.tvMessage.visibility = tvVisibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvCompleted.adapter = null
        _binding = null
    }
}