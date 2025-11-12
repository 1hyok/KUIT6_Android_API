package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.model.response.PostResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class PostEditViewModel(
    private val repository: PostRepository
) : ViewModel() {
    var postDetail by mutableStateOf<PostResponse?>(null)
        private set

    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

    var isUploading by mutableStateOf(false)
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
                    clearUploadedImageUrl()
                    onSuccess()
                }
        }
    }

    fun clearUploadedImageUrl() {
        uploadedImageUrl = null
    }

    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            isUploading = true
            runCatching {
                val file = UriUtils.uriToFile(context, uri)
                if (file == null) {
                    throw Exception("파일 변환 실패")
                }

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                repository.uploadImage(body)
            }.onSuccess { imageUrl ->
                isUploading = false
                uploadedImageUrl = imageUrl
                onSuccess(imageUrl)
            }.onFailure { error ->
                isUploading = false
                onError(error.message ?: "업로드 실패")
            }
        }
    }
}

