package com.example.offers.data.mapper

import com.example.core_data.models.OfferModel
import com.example.offers.data.remote.dto.OfferDto
import javax.inject.Inject

class ModelsMapper @Inject constructor() {
    fun mapDtoToUiModel(dto: OfferDto) = OfferModel(
        title = dto.title,
        description = dto.description,
        images = dto.images,
        thumbnail = dto.thumbnail,
        brand = dto.brand,
        rating = dto.rating.toString(),
        discount = dto.discountPercentage.toString(),
        price = dto.price.toString()
    )
}