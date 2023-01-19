package com.ekzak.composition.domain.repository

import com.ekzak.composition.domain.entity.GameLevel
import com.ekzak.composition.domain.entity.GameSettings
import com.ekzak.composition.domain.entity.Question

interface GameRepository {

    fun getGameSettings(level: GameLevel): GameSettings

    fun generateQuestion(maxSum: Int, countOfOptions: Int): Question
}
