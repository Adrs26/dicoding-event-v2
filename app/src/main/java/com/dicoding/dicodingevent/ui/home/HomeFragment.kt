package com.dicoding.dicodingevent.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayoutViewPager()
        setupButton()
    }

    private fun setupTabLayoutViewPager() {
        val fragments = listOf(UpcomingFragment(), CompletedFragment())
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle, fragments)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.upcoming_tab)
                1 -> resources.getString(R.string.completed_tab)
                else -> null
            }
        }.attach()
    }

    private fun setupButton() {
        binding.ibSearch.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_search_fragment)
        }
        binding.ibFavorite.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_favorite_fragment)
        }
    }
}