package com.ekzak.composition.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ekzak.composition.data.GameRepositoryImp
import com.ekzak.composition.domain.entity.GameLevel
import com.ekzak.composition.domain.entity.GameResult
import com.ekzak.composition.domain.entity.GameSettings
import com.ekzak.composition.domain.entity.Question
import com.ekzak.composition.domain.usecases.GenerateQuestionUseCase
import com.ekzak.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(gameLevel: GameLevel) : ViewModel() {

    private val repository = GameRepositoryImp
    private val getGameSettingUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private var gameSettings: GameSettings = getGameSettingUseCase.invoke(gameLevel)

    private val timerString = MutableLiveData<String>()
    private val progress = MutableLiveData<Int>()
    private val minCountRightAnswers = MutableLiveData<Int>()
    private val question = MutableLiveData<Question>()
    private val countRightAnswers = MutableLiveData<Int>()
    private val isAnswerRight = MutableLiveData<Boolean>()
    private val resultGame = MutableLiveData<GameResult>()
    private var totalQuestions = 0

    init {
        timerString.value = parseTime(gameSettings.gameTimeInSeconds)
        progress.value = 0
        countRightAnswers.value = 0
        minCountRightAnswers.value = gameSettings.minCountOfRightAnswers

        object : CountDownTimer(gameSettings.gameTimeInSeconds * MS_IN_ONE_SEC, MS_IN_ONE_SEC) {
            override fun onTick(millisUntilFinished: Long) {
                timerString.value = parseTime((millisUntilFinished / MS_IN_ONE_SEC).toInt())
            }

            override fun onFinish() {
                val isWinner = checkWinner()
                resultGame.value = GameResult(
                    isWinner,
                    countRightAnswers.value!!,
                    totalQuestions,
                    gameSettings
                )
            }
        }.start()
    }

    fun generateQuestion() {
        question.value = generateQuestionUseCase.invoke(gameSettings.maxSumValue)
        totalQuestions++
    }

    fun checkAnswer(answerText: String) {
        val answer = answerText.toInt()
        question.value?.let { question ->
            if (answer + question.visibleNumber == question.sum) {
                isAnswerRight.value = true
                countRightAnswers.value = countRightAnswers.value?.plus(1)
            }
        }
        calculateProgress()
    }

    fun observeGameTime(owner: LifecycleOwner, observer: Observer<String>) {
        timerString.observe(owner, observer)
    }

    fun observeResultGame(owner: LifecycleOwner, observer: Observer<GameResult>) {
        resultGame.observe(owner, observer)
    }

    fun observeProgressGame(owner: LifecycleOwner, observer: Observer<Int>) {
        progress.observe(owner, observer)
    }

    fun observeQuestion(owner: LifecycleOwner, observer: Observer<Question>) {
        question.observe(owner, observer)
    }

    fun observeAnswer(owner: LifecycleOwner, observer: Observer<Boolean>) {
        isAnswerRight.observe(owner, observer)
    }

    fun observeCountRightAnswer(owner: LifecycleOwner, observer: Observer<Int>) {
        countRightAnswers.observe(owner, observer)
    }

    fun observeMinRightAnswersSetting(owner: LifecycleOwner, observer: Observer<Int>) {
        minCountRightAnswers.observe(owner, observer)
    }

    private fun calculateProgress() {
        countRightAnswers.value?.let { countRightAnswers ->
            progress.value = (countRightAnswers.toDouble() / gameSettings.minCountOfRightAnswers * 100).toInt()
        }
    }

    private fun checkWinner(): Boolean {
        val rightPercentAnswers = (countRightAnswers.value!!.toDouble() / totalQuestions * 100).toInt()
        return countRightAnswers.value!! >= gameSettings.minCountOfRightAnswers &&
                rightPercentAnswers >= gameSettings.minPercentOfRightAnswers
    }

    private fun parseTime(timeInSeconds: Int): String {
        return String.format("%2d:%2d", timeInSeconds / SEC_IN_ONE_MIN, timeInSeconds % SEC_IN_ONE_MIN)
    }

    companion object {
        private const val MS_IN_ONE_SEC = 1000L
        private const val SEC_IN_ONE_MIN = 60
    }
}
