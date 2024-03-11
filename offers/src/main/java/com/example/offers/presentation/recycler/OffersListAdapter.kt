package com.example.offers.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import com.example.core_data.models.OfferModel
import com.example.offers.databinding.OfferItemBinding

class OffersListAdapter: PagingDataAdapter<OfferModel, OffersHolder>(
    OfferItemDiffCallBack
) {
    var onItemClickListener: ((OfferModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersHolder {
        val binding = OfferItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OffersHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: OffersHolder, position: Int) {
        val offerItem = getItem(position)
        val binding = viewHolder.binding
        val context = viewHolder.itemView.context

        Glide.with(context)
            .load(offerItem?.thumbnail)
            .into(binding.offerImage)

        binding.offerListItem.setOnClickListener {
            onItemClickListener?.invoke(offerItem!!)
        }

        binding.offetTitle.text = offerItem?.title
        binding.offerType.text = offerItem?.description
    }
}
