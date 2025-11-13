package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

//uiState를 통해 상태를 한 번에 모아 처리
data class PostCreateUiState(
    val uploadedImageUrl: String? = null,
    val isUploading: Boolean = false
)

class PostCreateViewModel(
    private val repository: PostRepository
) : ViewModel() {
    var uiState by mutableStateOf(PostCreateUiState())
        private set

    fun createPost(
        author: String,
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.createPost(author, title, content, imageUrl)
                .onSuccess {
                    uiState = uiState.copy(uploadedImageUrl = null)
                    onSuccess()
                }
        }
    }

    fun clearUploadedImageUrl() {
        uiState = uiState.copy(uploadedImageUrl = null)
    }

    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            uiState = uiState.copy(isUploading = true)
            runCatching {
                val file = UriUtils.uriToFile(context, uri)
                if (file == null) {
                    throw Exception("파일 변환 실패")
                }

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                repository.uploadImage(body)
            }.onSuccess { imageUrl ->
                uiState = uiState.copy(
                    isUploading = false,
                    uploadedImageUrl = imageUrl
                )
                onSuccess(imageUrl)
            }.onFailure { error ->
                uiState = uiState.copy(isUploading = false)
                onError(error.message ?: "업로드 실패")
            }
        }
    }
}

