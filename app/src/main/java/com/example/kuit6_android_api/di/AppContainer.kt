package com.example.kuit6_android_api.di

import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.repository.PostRepositoryImpl

class AppContainer {
    //모든 의존성을 AppContainer 한 곳에서 관리하게 함
    val apiService: ApiService = RetrofitClient.apiService
    //ApiService를 AppContainer에서 한 번만 가져 와 Repository에 주입
    // 원래 ApiService를 직접 참조하던 ViewModel들이 Repository를 참조
    val postRepository: PostRepository = PostRepositoryImpl(apiService)
}
