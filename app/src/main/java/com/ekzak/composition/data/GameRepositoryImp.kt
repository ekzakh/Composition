package com.ekzak.composition.data

import com.ekzak.composition.domain.entity.GameLevel
import com.ekzak.composition.domain.entity.GameSettings
import com.ekzak.composition.domain.entity.Question
import com.ekzak.composition.domain.repository.GameRepository
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.random.Random

object GameRepositoryImp : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun getGameSettings(level: GameLevel): GameSettings {
        return when (level) {
            GameLevel.TEST -> GameSettings(
                10,
                5,
                80,
                10
            )
            GameLevel.EASY -> GameSettings(
                10,
                10,
                70,
                60
            )
            GameLevel.NORMAL -> GameSettings(
                20,
                20,
                80,
                40
            )
            GameLevel.HARD -> GameSettings(
                30,
                30,
                90,
                40
            )
        }
    }

    override fun generateQuestion(maxSum: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSum + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)

        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)
        val to = min(maxSum - 1, rightAnswer + countOfOptions)

        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }

        return Question(sum, visibleNumber, options.toList())
    }

}
