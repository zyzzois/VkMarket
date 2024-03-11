package com.example.offer_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.models.SlideModel
import com.example.core_data.models.OfferModel
import com.example.offer_detail.databinding.FragmentOfferDetailBinding
import com.google.gson.Gson

class OfferDetailFragment : Fragment() {
    private var _binding: FragmentOfferDetailBinding? = null
    private val binding: FragmentOfferDetailBinding
        get() = _binding ?: throw RuntimeException("error")

    private val args by navArgs<OfferDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadOfferInfo()
    }

    private fun loadOfferInfo() {
        val gson = Gson()
        val offer = gson.fromJson(
            args.offer,
            OfferModel::class.java
        )
        showOfferDetailInfo(offer)
    }

    private fun showOfferDetailInfo(offer: OfferModel) {
        binding.imageSlider.setImageList(
            offer.images.map {
                SlideModel(it)
            }
        )
        binding.tvOfferTitle.text = offer.title
        binding.tvOfferDescription.text = offer.description
        binding.tvOfferBrand.text = offer.brand
        binding.tvOfferRating.text = offer.rating
        binding.ratingBar.rating = offer.rating.toFloat()
        binding.tvOfferDiscount.text = String.format(
            requireContext().getString(R.string.discount_text_placeholder), offer.discount
        )
        binding.tvOfferPrice.text = String.format(
            requireContext().getString(R.string.price_text_placeholder), offer.price
        )
    }

}