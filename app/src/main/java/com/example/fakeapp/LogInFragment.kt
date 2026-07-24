package com.example.cs426_seminar_app.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fakeapp.R
import com.example.fakeapp.databinding.OverlayLayoutBinding
import androidx.core.app.ActivityCompat.finishAffinity

class LogInFragment : Fragment() {
    private var _binding: OverlayLayoutBinding? = null
    private val binding get() = _binding!!
//    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OverlayLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.btnLogin.setOnClickListener {
//            attemptLogin()
            Toast.makeText(
                context,
                "Wrong password! Please try again",
                Toast.LENGTH_SHORT
            ).show()

            // and exit all current app tasks.
            requireActivity().finishAffinity()

        }

    }

    private fun attemptLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                context,
                "Please fill up the blank",
                Toast.LENGTH_SHORT
            ).show()

            return
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}