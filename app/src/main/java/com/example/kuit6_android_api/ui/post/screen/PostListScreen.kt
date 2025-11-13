package com.example.kuit6_android_api.ui.post.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kuit6_android_api.di.AppContainer
import com.example.kuit6_android_api.ui.post.component.PostItem
import com.example.kuit6_android_api.ui.post.viewmodel.PostListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    onPostClick: (Long) -> Unit,
    onCreatePostClick: () -> Unit,
    viewModel: PostListViewModel = viewModel<PostListViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val appContainer = AppContainer()
                return PostListViewModel(appContainer.postRepository) as T
            }
        }
    )
) {
    val uiState = viewModel.uiState
    val posts = uiState.posts
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {//lifecycleOwner가 변경될 때마다 실행
        //LaunchedEffect와 달리 화면이 처음 나타날 때뿐만 아니라 다시 나타날 때도 실행
        val observer = LifecycleEventObserver { _, event -> //lifecycle 이벤트를 감지하는 옵저버
            //event로 상태 변화 받음
            if (event == Lifecycle.Event.ON_RESUME) {//화면이 다시 활성화되면
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)//화면 상태가 바뀔 때마다 알림을 받기 위해 옵저버 등록
        onDispose {//화면이 사라질 때
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("게시글 목록") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreatePostClick) {
                Icon(Icons.Default.Add, contentDescription = "게시글 작성")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(posts) { post ->
                PostItem(
                    post = post,
                    onClick = { onPostClick(post.id) }
                )
            }
        }
    }
}
