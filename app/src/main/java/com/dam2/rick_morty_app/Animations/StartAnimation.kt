package com.dam2.rick_morty_app.Animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.BounceInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dam2.rick_morty_app.R
import com.dam2.rick_morty_app.Views.MainActivity
import com.dam2.rick_morty_app.databinding.ActivityStartAnimationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartAnimation : AppCompatActivity() {

    lateinit var b : ActivityStartAnimationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityStartAnimationBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CoroutineScope(Dispatchers.Main).launch {
            launch { rickAnimacion() }
            launch { animacionCaida() }
        }
    }

    /*
    * He tenido que poner todas las animaciones en hilos porque la animación sobrecargaba mi ordenador
    * y no se veia estable, además me lanzaba avisos sobre que skipeo de frames por sobre carga del
    * hilo main.
    */
    private suspend fun animacionCaida() {
        val tvRickAndMorty = b.tvRickAndMorty

        withContext(Dispatchers.Main) {
            val animacionDeCaida = ObjectAnimator.ofFloat(
                tvRickAndMorty,
                "translationY",
                -200f,
                700f
            ).apply {
                duration = 1800
                interpolator = BounceInterpolator()
            }

            val animacionDeRebote = ObjectAnimator.ofFloat(
                tvRickAndMorty,
                "translationY",
                600f,
                500f,
                600f
            ).apply {
                duration = 800
            }

            val escalaX = ObjectAnimator.ofFloat(
                tvRickAndMorty,
                "scaleX",
                1f, 1.1f, 1f
            ).apply {
                duration = 1200
            }

            val escalaY = ObjectAnimator.ofFloat(
                tvRickAndMorty,
                "scaleY",
                1f, 1.1f, 1f
            ).apply {
                duration = 1200
            }

            val fadeOut = ObjectAnimator.ofFloat(
                tvRickAndMorty,
                "alpha",
                1f, 0f
            ).apply {
                duration = 1200
            }

            AnimatorSet().apply {
                playSequentially(
                    animacionDeCaida,
                    animacionDeRebote,
                    escalaX,
                    escalaY,
                    fadeOut
                )
                start()
            }
        }
    }

    private suspend fun rickAnimacion() {
        val lottieAnimation = b.LottieAnimation

        withContext(Dispatchers.Main) {
            lottieAnimation.setAnimation(R.raw.rick_morty_animacion)
            lottieAnimation.playAnimation()
        }

        withContext(Dispatchers.IO) {
            delay(4100)
        }

        withContext(Dispatchers.Main) {
            startActivity(Intent(this@StartAnimation, MainActivity::class.java))
            finish()
        }
    }
}