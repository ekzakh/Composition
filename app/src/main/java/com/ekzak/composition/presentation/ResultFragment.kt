package com.ekzak.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ekzak.composition.R
import com.ekzak.composition.databinding.FragmentResultBinding
import com.ekzak.composition.domain.entity.GameResult

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding(FragmentResultBinding::bind)
    private lateinit var gameResult: GameResult

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseArgs()
        showGameResult()
        binding.retryButton.setOnClickListener {
            launchChooseLevelScreen()
        }
        handleBackPressed()
    }

    private fun parseArgs() {
        gameResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requireArguments().getSerializable(RESULT_KEY, GameResult::class.java)!!
        else @Suppress("DEPRECATION") requireArguments().getSerializable(RESULT_KEY) as GameResult
    }

    private fun showGameResult() {
        with(binding) {
            imageSmile.setImageDrawable(if (gameResult.winner) ContextCompat.getDrawable(requireActivity(),
                R.drawable.happy_smile) else ContextCompat.getDrawable(requireActivity(), R.drawable.sad_smile))
            tvRequiredCorrectAnswers.text =
                resources.getString(R.string.required_correct_answers, gameResult.gameSettings.minCountOfRightAnswers)
            tvRequiredPercentAnswers.text =
                resources.getString(R.string.required_percent_answers, gameResult.gameSettings.minPercentOfRightAnswers)
            tvCorrectAnswers.text = resources.getString(R.string.correct_answers, gameResult.countOfRightAnswers)
            tvPercent.text = resources.getString(R.string.correct_answers, 100) //TODO set correct value
        }
    }

    private fun handleBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                launchChooseLevelScreen()
            }
        })
    }

    private fun launchChooseLevelScreen() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        private const val RESULT_KEY = "result"
        fun newInstance(gameResult: GameResult): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(RESULT_KEY, gameResult)
                }
            }
        }
    }
}
