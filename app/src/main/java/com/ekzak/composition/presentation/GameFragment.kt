package com.ekzak.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ekzak.composition.R
import com.ekzak.composition.databinding.FragmentGameBinding
import com.ekzak.composition.domain.entity.GameLevel
import com.ekzak.composition.domain.entity.GameResult
import com.ekzak.composition.domain.entity.GameSettings

class GameFragment : Fragment(R.layout.fragment_game) {

    private lateinit var level: GameLevel
    private val binding by viewBinding(FragmentGameBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSum.setOnClickListener {
            launchResulScreen(GameResult(
                winner = true,
                countOfRightAnswers = 5,
                countOfQuestions = 10,
                gameSettings = GameSettings(
                    maxSumValue = 20,
                    minCountOfRightAnswers = 8,
                    minPercentOfRightAnswers = 8,
                    gameTimeInSeconds = 10)
            ))
        }
    }


    private fun launchResulScreen(result: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, ResultFragment.newInstance(result))
            .addToBackStack(null)
            .commit()
    }

    private fun parseArgs() {
        level = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requireArguments().getParcelable(KEY_LEVEL, GameLevel::class.java)!!
        else @Suppress("DEPRECATION") requireArguments().getParcelable<GameLevel>(KEY_LEVEL) as GameLevel
    }

    companion object {
        private const val KEY_LEVEL = "level"
        const val NAME = "game"

        fun newInstance(level: GameLevel): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}
