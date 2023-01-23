package com.ekzak.composition.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekzak.composition.domain.entity.GameLevel

class GameViewModelFactory(private val gameLevel: GameLevel) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = GameViewModel(gameLevel) as T
}
