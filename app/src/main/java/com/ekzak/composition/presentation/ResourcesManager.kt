package com.ekzak.composition.presentation

import androidx.annotation.StringRes

interface ResourcesManager {
    fun provideString(@StringRes id: Int, vararg args: Any): String
}
