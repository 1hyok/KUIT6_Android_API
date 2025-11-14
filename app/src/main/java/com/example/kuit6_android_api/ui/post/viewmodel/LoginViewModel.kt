package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kuit6_android_api.ui.post.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onIdChanged(id: String) {
        _uiState.update {
            it.copy(id = id)
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun onAutoLoginChanged(isAutoLogin: Boolean) {
        _uiState.update {
            it.copy(isAutoLogin = isAutoLogin)
        }
    }
}