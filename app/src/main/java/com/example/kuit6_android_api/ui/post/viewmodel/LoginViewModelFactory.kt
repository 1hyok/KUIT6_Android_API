package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kuit6_android_api.App
import com.example.kuit6_android_api.data.repository.LoginRepository

inline fun <reified VM : ViewModel> loginViewModelFactory(
    crossinline create: (LoginRepository) -> VM
): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                as App

        val loginRepository = application.container.loginRepository
        create(loginRepository)
    }
}