package com.dam2.rick_morty_app.Animations.RickFlotante

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView

class FloatingRickAnimation(
    val context : Context,
    val view : View,
    val imageView: ImageView
) {

    private val maxHeight = view.height.toFloat()
    private val width = view.width

    suspend fun animation() {
        val animacionCaida = ObjectAnimator.ofFloat(
            imageView,
            "translationY",
            0f,
            maxHeight
        ).apply {
            duration = 4000
            interpolator = BounceInterpolator()
        }


    }
}