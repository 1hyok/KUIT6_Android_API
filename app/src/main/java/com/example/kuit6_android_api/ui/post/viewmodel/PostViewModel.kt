package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.Author
import com.example.kuit6_android_api.data.model.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PostViewModel : ViewModel() {

    // 더미 데이터
    private val dummyPosts = mutableStateListOf(
        Post(
            id = 1,
            title = "Jetpack Compose 시작하기",
            content = "Jetpack Compose는 Android의 최신 UI 툴킷입니다. 선언형 UI로 더 쉽고 빠르게 UI를 만들 수 있습니다.",
            imageUrl = null,
            author = Author(1, "개발자A", null),
            createdAt = "2025-10-05T10:00:00",
            updatedAt = "2025-10-05T10:00:00"
        ),
        Post(
            id = 2,
            title = "Kotlin Coroutines 완벽 가이드",
            content = "비동기 프로그래밍을 쉽게! Coroutines를 사용하면 복잡한 비동기 코드를 간단하게 작성할 수 있습니다.",
            imageUrl = null,
            author = Author(2, "개발자B", null),
            createdAt = "2025-10-05T11:30:00",
            updatedAt = "2025-10-05T11:30:00"
        ),
        Post(
            id = 3,
            title = "Android MVVM 아키텍처",
            content = "MVVM 패턴으로 코드를 구조화하면 테스트와 유지보수가 쉬워집니다. ViewModel과 LiveData/StateFlow를 활용해봅시다.",
            imageUrl = null,
            author = Author(1, "개발자A", null),
            createdAt = "2025-10-05T14:20:00",
            updatedAt = "2025-10-05T14:20:00"
        )
    )

    private var nextId = 4L

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    var postDetail by mutableStateOf<Post?>(null)
        private set

    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

    fun getPosts() {
        viewModelScope.launch {
            delay(500) // 네트워크 시뮬레이션
            posts = dummyPosts.toList()
        }
    }

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            delay(300)
            postDetail = dummyPosts.find { it.id == postId }
        }
    }

    fun createPost(
        author: String = "anonymous",
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            delay(500)
            val newPost = Post(
                id = nextId++,
                title = title,
                content = content,
                imageUrl = imageUrl,
                author = Author(nextId, author, null),
                createdAt = getCurrentDateTime(),
                updatedAt = getCurrentDateTime()
            )
            dummyPosts.add(0, newPost)
            posts = dummyPosts.toList()
            onSuccess()
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
            delay(500)
            val index = dummyPosts.indexOfFirst { it.id == postId }
            if (index != -1) {
                val oldPost = dummyPosts[index]
                val updatedPost = oldPost.copy(
                    title = title,
                    content = content,
                    imageUrl = imageUrl,
                    updatedAt = getCurrentDateTime()
                )
                dummyPosts[index] = updatedPost
                postDetail = updatedPost
                posts = dummyPosts.toList()
                onSuccess()
            }
        }
    }

    fun deletePost(postId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            delay(300)
            dummyPosts.removeIf { it.id == postId }
            posts = dummyPosts.toList()
            onSuccess()
        }
    }

    fun clearUploadedImageUrl() {
        uploadedImageUrl = null
    }

    private fun getCurrentDateTime(): String {
        return LocalDateTime.now().toString()
    }
}
