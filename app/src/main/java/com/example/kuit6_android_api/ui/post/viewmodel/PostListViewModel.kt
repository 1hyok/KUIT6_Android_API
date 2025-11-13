package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.model.response.PostResponse
import kotlinx.coroutines.launch

data class PostListUiState(
    val posts: List<PostResponse> = emptyList()
)

class PostListViewModel(
    private val repository: PostRepository
) : ViewModel() {
    var uiState by mutableStateOf(PostListUiState())
        private set

    fun refresh() {
        viewModelScope.launch {
            repository.getPosts()
                .onSuccess { posts ->
                    uiState = uiState.copy(posts = posts)
                }
                .onFailure {
                    uiState = uiState.copy(posts = emptyList())
                }
        }
    }
}
