package com.example.offers.presentation

import androidx.navigation.NavOptions

val navOptions = NavOptions.Builder()
    .setEnterAnim(com.example.core_ui.R.anim.slide_in_right)
    .setExitAnim(com.example.core_ui.R.anim.slide_out_left)
    .setPopEnterAnim(com.example.core_ui.R.anim.slide_in_left)
    .setPopExitAnim(com.example.core_ui.R.anim.slide_out_right)
    .build()