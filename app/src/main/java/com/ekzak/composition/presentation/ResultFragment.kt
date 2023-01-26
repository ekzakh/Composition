package com.ekzak.composition.presentation

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ekzak.composition.R
import com.ekzak.composition.databinding.FragmentResultBinding

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding(FragmentResultBinding::bind)
    private val args by navArgs<ResultFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showGameResult()
        binding.retryButton.setOnClickListener {
            retryGame()
        }
    }

    private fun showGameResult() {
        val gameResult = args.result
        with(binding) {
            imageSmile.setImageDrawable(if (gameResult.winner) ContextCompat.getDrawable(requireActivity(),
                R.drawable.happy_smile) else ContextCompat.getDrawable(requireActivity(), R.drawable.sad_smile))
            tvRequiredCorrectAnswers.text =
                resources.getString(R.string.required_correct_answers, gameResult.gameSettings.minCountOfRightAnswers)
            tvRequiredPercentAnswers.text =
                resources.getString(R.string.required_percent_answers, gameResult.gameSettings.minPercentOfRightAnswers)
            tvCorrectAnswers.text = resources.getString(R.string.correct_answers, gameResult.countOfRightAnswers)
            tvPercent.text = resources.getString(R.string.correct_answers,
                (gameResult.countOfRightAnswers.toDouble() / gameResult.countOfQuestions * 100).toInt())
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }
}
