package com.ekzak.composition.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ekzak.composition.R
import com.ekzak.composition.data.GameRepositoryImp
import com.ekzak.composition.domain.entity.GameLevel
import com.ekzak.composition.domain.entity.GameResult
import com.ekzak.composition.domain.entity.GameSettings
import com.ekzak.composition.domain.entity.Question
import com.ekzak.composition.domain.usecases.GenerateQuestionUseCase
import com.ekzak.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val resourceManager: ResourcesManager,
    level: GameLevel,
    ) : ViewModel() {

    private val repository = GameRepositoryImp
    private val getGameSettingUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private var gameSettings: GameSettings
    private val formattedTime = MutableLiveData<String>()
    private val progressPercent = MutableLiveData<Int>()
    private val enoughCount = MutableLiveData<Boolean>()
    private val enoughPercent = MutableLiveData<Boolean>()
    private val minCount = MutableLiveData<Int>()
    private val minPercent = MutableLiveData<Int>()
    private val question = MutableLiveData<Question>()
    private val progressAnswers = MutableLiveData<String>()
    private val resultGame = MutableLiveData<GameResult>()
    private var timer: CountDownTimer? = null

    private var countRightAnswers = 0
    private var totalQuestions = 0

    init {
        gameSettings = getGameSettingUseCase.invoke(level)
        minPercent.value = gameSettings.minPercentOfRightAnswers
        minCount.value = gameSettings.minCountOfRightAnswers
        if (timer == null) {
            initTimer()
            generateQuestion()
        }
    }

    fun checkAnswer(answerText: String) {
        val answer = answerText.toInt()
        question.value?.let { question ->
            if (answer + question.visibleNumber == question.sum) {
                countRightAnswers++
            }
        }
        updateProgress()
        generateQuestion()
    }

    fun observeGameTime(owner: LifecycleOwner, observer: Observer<String>) {
        formattedTime.observe(owner, observer)
    }

    fun observeResultGame(owner: LifecycleOwner, observer: Observer<GameResult>) {
        resultGame.observe(owner, observer)
    }

    fun observeProgressPercent(owner: LifecycleOwner, observer: Observer<Int>) {
        progressPercent.observe(owner, observer)
    }

    fun observeQuestion(owner: LifecycleOwner, observer: Observer<Question>) {
        question.observe(owner, observer)
    }

    fun observeEnoughCount(owner: LifecycleOwner, observer: Observer<Boolean>) {
        enoughCount.observe(owner, observer)
    }

    fun observeEnoughPercent(owner: LifecycleOwner, observer: Observer<Boolean>) {
        enoughPercent.observe(owner, observer)
    }

    fun observeProgressAnswers(owner: LifecycleOwner, observer: Observer<String>) {
        progressAnswers.observe(owner, observer)
    }

    private fun initTimer() {
        timer = object : CountDownTimer(gameSettings.gameTimeInSeconds * MS_IN_ONE_SEC, MS_IN_ONE_SEC) {
            override fun onTick(millisUntilFinished: Long) {
                formattedTime.value = formatTime((millisUntilFinished / MS_IN_ONE_SEC).toInt())
            }

            override fun onFinish() {
                val isWinner = enoughCount.value == true && enoughPercent.value == true
                resultGame.value = GameResult(
                    isWinner,
                    countRightAnswers,
                    totalQuestions,
                    gameSettings
                )
            }
        }
        timer?.start()
    }

    private fun generateQuestion() {
        question.value = generateQuestionUseCase.invoke(gameSettings.maxSumValue)
        totalQuestions++
    }

    private fun updateProgress() {
        progressPercent.value = (countRightAnswers.toDouble() / totalQuestions * 100).toInt()
        progressAnswers.value = resourceManager.provideString(R.string.progress_answers,
            countRightAnswers,
            gameSettings.minCountOfRightAnswers)
        enoughCount.value = countRightAnswers >= gameSettings.minCountOfRightAnswers
        enoughPercent.value =
            (countRightAnswers.toDouble() / countRightAnswers * 100) >= gameSettings.minPercentOfRightAnswers
    }

    private fun formatTime(timeInSeconds: Int): String {
        return String.format("%02d:%02d", timeInSeconds / SEC_IN_ONE_MIN, timeInSeconds % SEC_IN_ONE_MIN)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MS_IN_ONE_SEC = 1000L
        private const val SEC_IN_ONE_MIN = 60
    }
}
