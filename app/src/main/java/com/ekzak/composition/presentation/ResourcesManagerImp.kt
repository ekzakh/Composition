package com.ekzak.composition.presentation

import android.content.Context

class ResourcesManagerImp(private val context: Context): ResourcesManager {
    override fun provideString(id: Int, vararg args: Any): String {
        return context.resources.getString(id, *args)
    }
}
