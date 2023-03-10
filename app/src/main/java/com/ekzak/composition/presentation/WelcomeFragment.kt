package com.ekzak.composition.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ekzak.composition.R
import com.ekzak.composition.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private val binding by viewBinding(FragmentWelcomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.understandButton.setOnClickListener {
            launchLevelScreen()
        }
    }

    private fun launchLevelScreen() {
        findNavController().navigate(R.id.action_welcomeFragment_to_chooseLevelFragment)
    }
}
