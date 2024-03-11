package com.example.core_data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferModel(
    val title: String,
    val description: String,
    val images: List<String>,
    val thumbnail: String,
    val brand: String,
    val rating: String,
    val discount: String,
    val price: String
): Parcelable
