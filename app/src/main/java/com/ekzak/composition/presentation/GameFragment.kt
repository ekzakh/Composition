package com.ekzak.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ekzak.composition.R
import com.ekzak.composition.databinding.FragmentGameBinding
import com.ekzak.composition.domain.entity.GameLevel
import com.ekzak.composition.domain.entity.GameResult

class GameFragment : Fragment(R.layout.fragment_game) {

    private lateinit var level: GameLevel
    private val binding by viewBinding(FragmentGameBinding::bind)

    private val viewModel by lazy {
        GameViewModelFactory(ResourcesManagerImp(requireContext()), level).create(GameViewModel::class.java)
    }
    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.option1)
            add(binding.option2)
            add(binding.option3)
            add(binding.option4)
            add(binding.option5)
            add(binding.option6)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOptionsListeners()
    }

    private fun setObservers() {
        viewModel.observeGameTime(viewLifecycleOwner) { binding.tvTime.text = it }
        viewModel.observeResultGame(viewLifecycleOwner) { launchResulScreen(it) }
        viewModel.observeProgressPercent(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }
        viewModel.observeQuestion(viewLifecycleOwner) { question ->
            with(binding) {
                tvSum.text = question.sum.toString()
                tvVisibleNumber.text = question.visibleNumber.toString()
                tvOptions.forEachIndexed { index, optionView ->
                    optionView.text = question.options[index].toString()
                }
            }
        }
        viewModel.observeEnoughPercent(viewLifecycleOwner) { isDone ->
            binding.progressBar.setIndicatorColor(getColorByState(isDone))
        }
        viewModel.observeEnoughCount(viewLifecycleOwner) { isDone ->
            binding.tvProgressAnswers.setTextColor(getColorByState(isDone))
        }
        viewModel.observeProgressAnswers(viewLifecycleOwner) { progressText ->
            binding.tvProgressAnswers.text = progressText
        }
    }

    private fun getColorByState(isDone: Boolean): Int {
        val color = if (isDone) android.R.color.holo_green_light else android.R.color.holo_red_light
        return ContextCompat.getColor(requireContext(), color)
    }

    private fun setOptionsListeners() {
        with(binding) {
            option1.setOnClickListener { viewModel.checkAnswer(option1.text.toString()) }
            option2.setOnClickListener { viewModel.checkAnswer(option2.text.toString()) }
            option3.setOnClickListener { viewModel.checkAnswer(option3.text.toString()) }
            option4.setOnClickListener { viewModel.checkAnswer(option4.text.toString()) }
            option5.setOnClickListener { viewModel.checkAnswer(option5.text.toString()) }
            option6.setOnClickListener { viewModel.checkAnswer(option6.text.toString()) }
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

