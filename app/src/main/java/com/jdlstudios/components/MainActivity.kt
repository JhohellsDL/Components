package com.jdlstudios.components

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.jdlstudios.components.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.buttonAgain.setOnClickListener {
            //val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.progress_bar_animation)
            //val animation2: Animation = AnimationUtils.loadAnimation(this, R.anim.progress_bar_animation_2)
            //binding.progressBar.startAnimation(animation)

            // Crea un ObjectAnimator para animar el progreso
            val progressAnimator = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, 60)
            progressAnimator.duration = 1000 // Duración de la animación en milisegundos

            progressAnimator.start()

            val valueAnimator = ValueAnimator.ofInt(0, 60)
            valueAnimator.duration = 1000 // Duración de la animación en milisegundos
            valueAnimator.addUpdateListener {
                val animatedValue = it.animatedValue as Int
                binding.textTest.text = animatedValue.toString()
            }

            // Inicia la animación
            valueAnimator.start()

        }


    }
}