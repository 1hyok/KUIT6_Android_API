package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.ui.post.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {
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

    fun signup() {
        viewModelScope.launch {
            val result = loginRepository.signup(
                id = uiState.value.id,
                password = uiState.value.password
            )
        }
    }

    fun login() {
        viewModelScope.launch {
            val result = loginRepository.login(
                id = uiState.value.id,
                password = uiState.value.password
            )
        }
    }


}