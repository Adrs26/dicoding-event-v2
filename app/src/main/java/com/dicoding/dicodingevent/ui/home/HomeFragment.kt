package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var viewPagerAdapter: FragmentStateAdapter? = null
    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayoutViewPager()
        setupButton()
    }

    private fun setupTabLayoutViewPager() {
        viewPagerAdapter = object : FragmentStateAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        ) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> UpcomingFragment()
                    1 -> CompletedFragment()
                    else -> throw IllegalStateException("Invalid position")
                }
            }
        }
        binding.viewPager.adapter = viewPagerAdapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.upcoming_tab)
                1 -> resources.getString(R.string.completed_tab)
                else -> null
            }
        }
        tabMediator?.attach()
    }

    private fun setupButton() {
        binding.ibSearch.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_search_fragment)
        }
        binding.ibFavorite.setOnClickListener {
            startActivity(Intent().setClassName(
                requireContext(),
                "com.dicoding.favorite.FavoriteActivity")
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator?.detach()
        tabMediator = null
        binding.viewPager.adapter = null
        viewPagerAdapter = null
        _binding = null
    }
}