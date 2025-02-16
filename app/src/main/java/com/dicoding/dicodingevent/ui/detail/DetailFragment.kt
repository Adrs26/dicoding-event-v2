package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.core.data.remote.network.ApiResponse
import com.dicoding.dicodingevent.core.domain.model.Event
import com.dicoding.dicodingevent.core.util.DataHelper
import com.dicoding.dicodingevent.core.util.DateHelper.convertDate
import com.dicoding.dicodingevent.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val detailViewModel: DetailViewModel by viewModels()
    private var isFavorite = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        getEventDetail()
    }

    private fun setupButton() {
        binding.ibBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ibFavorite.setOnClickListener {
            lifecycleScope.launch {
                if (isFavorite) {
                    detailViewModel.deleteFavoriteEvent()
                    setupToast(getString(R.string.event_removed_from_favorites))
                } else {
                    detailViewModel.insertFavoriteEvent()
                    setupToast(getString(R.string.event_saved_to_favorites))
                }
            }
        }
    }

    private fun getEventDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.eventDetail.collectLatest { state ->
                    when (state) {
                        is ApiResponse.Loading -> setUiVisibility(View.VISIBLE, View.GONE, View.GONE)
                        is ApiResponse.Empty -> setUiVisibility(View.GONE, View.GONE, View.VISIBLE)
                        is ApiResponse.Success -> {
                            setUiVisibility(View.GONE, View.VISIBLE, View.GONE)
                            setEventDetail(state.data)
                        }
                        is ApiResponse.Error -> {
                            setUiVisibility(View.GONE, View.GONE, View.VISIBLE)
                            binding.tvMessage.text = getString(R.string.no_internet_connection)
                        }
                    }
                }
            }
        }

        detailViewModel.isFavorite.observe(viewLifecycleOwner) { status ->
            isFavorite = status
            binding.ibFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_unfavorite
            )
        }
    }

    private fun setUiVisibility(pbVisibility: Int, ccVisibility: Int, tvVisibility: Int) {
        binding.pbDetail.visibility = pbVisibility
        binding.contentContainer.visibility = ccVisibility
        binding.cvButton.visibility = ccVisibility
        binding.tvMessage.visibility = tvVisibility
    }

    private fun setEventDetail(event: Event) {
        Glide.with(requireContext())
            .load(event.mediaCover)
            .centerCrop()
            .into(binding.ivEventImage)

        binding.apply {
            tvEventTitle.text = event.name
            tvEventOwnerName.text = event.ownerName
            tvEventCategoryName.text = event.category
            tvEventDateBegin.text = convertDate(requireContext(), event.beginTime, event.endTime)
            tvEventCityName.text = event.cityName
            (event.quota - event.registrants).toString().also {
                tvEventQuotaRemaining.text = it
            }
            tvEventDescriptionText.text = Html.fromHtml(
                event.description,
                Html.FROM_HTML_MODE_LEGACY
            )
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(event.link) }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }

        DataHelper.event = event
    }

    private fun setupToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}