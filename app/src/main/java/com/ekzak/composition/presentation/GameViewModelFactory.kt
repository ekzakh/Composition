package com.ekzak.composition.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekzak.composition.domain.entity.GameLevel

class GameViewModelFactory(
    private val resourcesManager: ResourcesManager,
    private val level: GameLevel,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        GameViewModel::class.java -> GameViewModel(resourcesManager, level) as T
        else -> throw IllegalArgumentException("Unknown view model class $modelClass")
    }
}
