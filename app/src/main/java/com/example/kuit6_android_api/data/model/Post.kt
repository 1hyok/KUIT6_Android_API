package com.example.kuit6_android_api.data.model

data class Author(
    val id: Long,
    val username: String,
    val profileImageUrl: String?
)

data class Post(
    val id: Long,
    val title: String,
    val content: String,
    val imageUrl: String?,
    val author: Author,
    val createdAt: String,
    val updatedAt: String
)
