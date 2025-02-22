package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dicoding.core.data.remote.network.ApiResponse
import com.dicoding.core.domain.model.Event
import com.dicoding.core.util.DataHelper
import com.dicoding.core.util.DateHelper.convertDate
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels()
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        getEventDetail()
    }

    private fun setupButton() {
        binding.ibBack.setOnClickListener {
            if (DataHelper.menuId == 0) {
                findNavController().navigateUp()
            } else {
                DataHelper.menuId = 0
                requireActivity().finish()
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}