package com.ekzak.composition.domain.usecases

import com.ekzak.composition.domain.entity.GameLevel
import com.ekzak.composition.domain.entity.GameSettings
import com.ekzak.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(level: GameLevel): GameSettings {
        return repository.getGameSettings(level)
    }
}
