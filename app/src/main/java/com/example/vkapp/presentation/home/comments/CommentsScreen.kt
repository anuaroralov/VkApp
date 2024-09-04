package com.example.vkapp.presentation.home.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkapp.R
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.PostComment
import com.example.vkapp.presentation.VkApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    onBackPressed: () -> Unit,
    feedPost: FeedPost,
) {
    val component = (LocalContext.current.applicationContext as VkApplication)
        .component
        .getCommentsScreenComponentFactory()
        .create(feedPost)

    val viewModel: CommentsViewModel = viewModel(factory = component.getViewModelFactory())
    val screenState by viewModel.screenState.collectAsState(CommentsScreenState.Initial)
    var commentText by remember { mutableStateOf("") }
    var replyingToComment by remember { mutableStateOf<PostComment?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.comments_title),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text(text = "Write a comment...", color = Color.Gray) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        disabledTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            // viewModel.addComment(feedPost, commentText, replyingToComment?.id)
                            commentText = ""
                            replyingToComment = null
                        }
                    },
                    enabled = commentText.isNotBlank()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_send_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (commentText.isNotBlank()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Gray
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (val currentState = screenState) {
            is CommentsScreenState.Comments -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 72.dp
                    )
                ) {
                    currentState.comments.forEach { comment ->
                        item(key = comment.id) {
                            CommentItem(
                                comment = comment,
                                onReply = { replyingToComment = it },
                            )
                        }

                        comment.replies?.let { replies ->
                            items(replies.items, key = { it.id }) { reply ->
                                CommentItem(
                                    comment = reply,
                                    onReply = { replyingToComment = it },
                                    isReply = true
                                )
                            }
                            if (replies.count != replies.items.size) {
                                item {
                                    val remainingReplies = replies.count - replies.items.size
                                    val text =
                                        if (remainingReplies == 1) "Show $remainingReplies more reply"
                                        else "Show $remainingReplies more replies"
                                    Text(
                                        text = text,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(48.dp, 2.dp, 0.dp, 4.dp)
                                            .clickable { viewModel.loadReplies(comment, feedPost) }
                                    )
                                }

                            }
                            item {
                                if (comment.nextDataIsLoading) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator(color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        if (currentState.nextDataIsLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(color = Color.Gray)
                            }
                        } else if (currentState.hasMoreComments) {
                            SideEffect {
                                viewModel.loadNextComments(feedPost)
                            }
                        }
                    }
                }
            }

            CommentsScreenState.Initial -> {}
            CommentsScreenState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = Color.Gray)
                }
            }
        }
    }
}


