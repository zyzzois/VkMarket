package com.example.offers.data.remote.dto

data class OffersDto(
    val limit: Int,
    val products: List<OfferDto>,
    val skip: Int,
    val total: Int
)