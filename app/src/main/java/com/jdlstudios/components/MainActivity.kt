package com.jdlstudios.components

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.jdlstudios.components.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object: BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    showToast("Authentication success!")
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    showToast("Authentication error: $errorCode")
                }
            }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animateProgressBarCircle()
        //checkBiometricSupport()
        binding.buttonCardFingerprint.setOnClickListener {
            //configurationBiometricPrompt()
            animateProgressBarCircle()
        }

    }

    private fun animateProgressBarCircle() {
        val progressAnimator = ObjectAnimator.ofInt(binding.progressBar1, "progress", 0, 60)
        val progressAnimator2 = ObjectAnimator.ofInt(binding.progressBar2, "progress", 0, 100)
        progressAnimator.duration = 1000 // Duración de la animación en milisegundos
        progressAnimator2.duration = 1000
        progressAnimator2.start()
        progressAnimator.start()
    }
    private fun animeProgressBar(){
        val progressAnimator = ObjectAnimator.ofInt(binding.progressBar1, "progress", 0, 60)
        progressAnimator.duration = 1000 // Duración de la animación en milisegundos

        progressAnimator.start()

        val valueAnimator = ValueAnimator.ofInt(0, 60)
        valueAnimator.duration = 1000 // Duración de la animación en milisegundos
        valueAnimator.addUpdateListener {
            val animatedValue = it.animatedValue as Int
            binding.textTest.text = animatedValue.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun configurationBiometricPrompt() {
        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Title")
            .setSubtitle("Subtitle")
            .setDescription("Description")
            .setNegativeButton("Cancel", this.mainExecutor) {_, _ ->
                showToast("Authentication cancelled")
            }.build()

        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            showToast("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }
    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            showToast("Fingerprint authentication has not been enabled in settings")
            return false
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            showToast("Fingerprint authentication permission is not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }
}