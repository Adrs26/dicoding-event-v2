package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.dicoding.core.data.remote.network.ApiResponse
import com.dicoding.core.domain.model.Event
import com.dicoding.core.util.DateHelper.convertDate
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var event: Event? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewBinding()
        setupButton()
        getEventDetail()
    }

    private fun setupViewBinding() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupButton() {
        binding.ibBack.setOnClickListener {
            finish()
        }

        binding.ibFavorite.setOnClickListener {
            lifecycleScope.launch {
                if (isFavorite) {
                    intent.extras?.getInt(EXTRA_ID, 0)?.let { eventId ->
                        detailViewModel.deleteFavoriteEvent(eventId)
                    }
                    setupToast(getString(R.string.event_removed_from_favorites))
                } else {
                    event?.let { event ->
                        detailViewModel.insertFavoriteEvent(event)
                    }
                    setupToast(getString(R.string.event_saved_to_favorites))
                }
            }
        }
    }

    private fun getEventDetail() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                intent.extras?.getInt(EXTRA_ID, 0)?.let { eventId ->
                    detailViewModel.checkIsEventFavorite(eventId)
                    detailViewModel.getEventDetail(eventId).collectLatest { state ->
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
        }

        detailViewModel.isFavorite.observe(this) { status ->
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
        Glide.with(this)
            .load(event.mediaCover)
            .centerCrop()
            .into(binding.ivEventImage)

        binding.apply {
            tvEventTitle.text = event.name
            tvEventOwnerName.text = event.ownerName
            tvEventCategoryName.text = event.category
            tvEventDateBegin.text = convertDate(this@DetailActivity, event.beginTime, event.endTime)
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
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        this.event = event
    }

    private fun setupToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}