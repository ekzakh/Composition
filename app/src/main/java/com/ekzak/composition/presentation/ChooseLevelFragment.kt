package com.ekzak.composition.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ekzak.composition.R
import com.ekzak.composition.databinding.FragmentChooseLevelBinding
import com.ekzak.composition.domain.entity.GameLevel

class ChooseLevelFragment : Fragment(R.layout.fragment_choose_level) {

    private val binding by viewBinding(FragmentChooseLevelBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            testButton.setOnClickListener { launchGameScreen(GameLevel.TEST) }
            easyButton.setOnClickListener { launchGameScreen(GameLevel.EASY) }
            normalButton.setOnClickListener { launchGameScreen(GameLevel.NORMAL) }
            hardButton.setOnClickListener { launchGameScreen(GameLevel.HARD) }
        }
    }

    private fun launchGameScreen(level: GameLevel) {
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level)
        )
    }
}
