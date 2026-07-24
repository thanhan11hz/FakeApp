package com.example.fakeapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fakeapp.databinding.ActivityFakeLoginBinding
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.util.Log

class FakeLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFakeLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        val t = System.currentTimeMillis()

        super.onCreate(savedInstanceState)

        Log.d("TIME", "after super = ${System.currentTimeMillis()-t}")

        binding = ActivityFakeLoginBinding.inflate(layoutInflater)

        Log.d("TIME", "after inflate = ${System.currentTimeMillis()-t}")

        setContentView(binding.root)

        Log.d("TIME", "after setContent = ${System.currentTimeMillis()-t}")

        window.setWindowAnimations(0)
        binding = ActivityFakeLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        binding.btnLogin.setOnClickListener {

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            binding.tvError.visibility = android.view.View.VISIBLE

            Toast.makeText(
                this,
                "Wrong password!",
                Toast.LENGTH_SHORT
            ).show()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

            Handler(Looper.getMainLooper()).postDelayed({
                finish()

                if (android.os.Build.VERSION.SDK_INT >= 34) {
                    overrideActivityTransition(
                        OVERRIDE_TRANSITION_CLOSE,
                        R.anim.no_anim,
                        R.anim.no_anim

                    )
                } else {
                    @Suppress("DEPRECATION")
                    overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
                }
            }, 2000)
        }
    }
}