package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.model.response.PostResponse
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val repository: PostRepository
) : ViewModel() {
    var postDetail by mutableStateOf<PostResponse?>(null)
        private set

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            repository.getPostDetail(postId)
                .onSuccess { post ->
                    postDetail = post
                }
                .onFailure {
                    postDetail = null
                }
        }
    }

    fun deletePost(postId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deletePost(postId)
                .onSuccess {
                    onSuccess()
                }
        }
    }
}

