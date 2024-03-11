package com.example.offers.presentation.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.core_data.models.OfferModel

object OfferItemDiffCallBack: DiffUtil.ItemCallback<OfferModel>() {
    override fun areItemsTheSame(oldItem: OfferModel, newItem: OfferModel): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: OfferModel, newItem: OfferModel): Boolean {
        return oldItem == newItem
    }
}
