package com.example.kuit6_android_api.data.model.response

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName(value="id") val id:Long,
    @SerializedName(value="title") val title:String,
    @SerializedName(value="content") val content:String,
    @SerializedName(value="imageUrl") val imageUrl:String?,
    @SerializedName(value="author") val author: AuthorResponse,
    @SerializedName(value="createdAt") val createdAt:String,
    @SerializedName(value="updatedAt") val updatedAt:String
)
