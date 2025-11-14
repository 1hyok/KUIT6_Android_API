package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.model.response.PostResponse
import com.example.kuit6_android_api.util.UriUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

data class PostEditUiState(
    val postDetail: PostResponse? = null,
    val uploadedImageUrl: String? = null,
    val isUploading: Boolean = false
)

class PostEditViewModel(
    private val repository: PostRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostEditUiState())
    val uiState: StateFlow<PostEditUiState> = _uiState.asStateFlow()

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            repository.getPostDetail(postId)
                .onSuccess { post ->
                    _uiState.update { it.copy(postDetail = post) }
                }
                .onFailure {
                    _uiState.update { it.copy(postDetail = null) }
                }
        }
    }

    fun updatePost(
        postId: Long,
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.updatePost(postId, title, content, imageUrl)
                .onSuccess {
                    _uiState.update { it.copy(uploadedImageUrl = null) }
                    onSuccess()
                }
        }
    }

    fun clearUploadedImageUrl() {
        _uiState.update { it.copy(uploadedImageUrl = null) }
    }

    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true) }
            val file = UriUtils.uriToFile(context, uri)
            if (file == null) {
                _uiState.update { it.copy(isUploading = false) }
                onError("파일 변환 실패")
                return@launch
            }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            repository.uploadImage(body)
                .onSuccess { imageUrl ->
                    _uiState.update {
                        it.copy(
                            isUploading = false,
                            uploadedImageUrl = imageUrl
                        )
                    }
                    onSuccess(imageUrl)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isUploading = false) }
                    onError(error.message ?: "업로드 실패")
                }
        }
    }
}

