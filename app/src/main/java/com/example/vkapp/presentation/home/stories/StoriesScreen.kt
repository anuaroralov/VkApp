package com.example.vkapp.presentation.home.stories

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import com.example.vkapp.R
import kotlin.math.max
import kotlin.math.min

@OptIn(UnstableApi::class)
@Composable
fun StoriesScreen() {
    val images = remember {
        listOf(
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background
        )
    }

    val videos = remember {
        listOf(
            Uri.parse("https://vkvd187.mycdn.me/video.m3u8?srcIp=93.170.37.72&pr=48&expires=1723346755107&srcAg=CHROME&fromCache=1&ms=185.226.53.187&mid=10422018584576&type=2&sig=5zxgXDQAdvw&ct=8&urls=45.136.21.159&clientType=13&zs=14&cmd=videoPlayerCdn&id=7800722229760"),
            Uri.parse("https://vkvd187.mycdn.me/video.m3u8?srcIp=93.170.37.72&pr=48&expires=1723346755107&srcAg=CHROME&fromCache=1&ms=185.226.53.187&mid=10422018584576&type=2&sig=5zxgXDQAdvw&ct=8&urls=45.136.21.159&clientType=13&zs=14&cmd=videoPlayerCdn&id=7800722229760")
        )
    }

    val stepCount = images.size + videos.size
    val currentStep = remember { mutableIntStateOf(0) }
    val isPaused = remember { mutableStateOf(false) }
    val videoDuration = remember { mutableIntStateOf(5000) } // Default duration for images

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val mediaModifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        currentStep.intValue = if (offset.x < constraints.maxWidth / 2) {
                            max(0, currentStep.intValue - 1)
                        } else {
                            min(stepCount - 1, currentStep.intValue + 1)
                        }
                        isPaused.value = false
                    },
                    onPress = {
                        try {
                            isPaused.value = true
                            awaitRelease()
                        } finally {
                            isPaused.value = false
                        }
                    }
                )
            }

        if (currentStep.intValue < images.size) {
            Image(
                painter = painterResource(id = images[currentStep.intValue]),
                contentDescription = "StoryImage",
                contentScale = ContentScale.FillHeight,
                modifier = mediaModifier
            )
            videoDuration.value = 2000 // Duration for images
        } else {
            val context = LocalContext.current
            val player = remember {
                ExoPlayer.Builder(context).build().apply {
                    val dataSourceFactory = DefaultHttpDataSource.Factory()
                    val mediaSourceFactory = HlsMediaSource.Factory(dataSourceFactory)
                    val mediaItem = MediaItem.fromUri(videos[currentStep.intValue - images.size])
                    val mediaSource = mediaSourceFactory.createMediaSource(mediaItem)
                    setMediaSource(mediaSource)
                    playWhenReady = true // Автоматическое воспроизведение видео
                    prepare()
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    player.release()
                }
            }

            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        videoDuration.value = player.duration.toInt()
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (!isPlaying && !isPaused.value) {
                        currentStep.intValue = (currentStep.intValue + 1) % stepCount
                    }
                }
            })

            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        this.player = player
                        useController = false // Отключение элементов управления
                    }
                },
                modifier = mediaModifier
            )

            LaunchedEffect(isPaused.value) {
                if (isPaused.value) {
                    player.pause()
                } else {
                    player.play()
                }
            }
        }

        InstagramProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            stepCount = stepCount,
            stepDuration = videoDuration.value,
            unSelectedColor = Color.LightGray,
            selectedColor = Color.White,
            currentStep = currentStep.intValue,
            onStepChanged = { currentStep.intValue = it },
            isPaused = isPaused.value,
            onComplete = { }
        )
    }
}

@Composable
fun InstagramProgressIndicator(
    modifier: Modifier = Modifier,
    stepCount: Int,
    stepDuration: Int,
    unSelectedColor: Color,
    selectedColor: Color,
    currentStep: Int,
    onStepChanged: (Int) -> Unit,
    isPaused: Boolean = false,
    onComplete: () -> Unit,
) {
    val currentStepState = remember(currentStep) { mutableIntStateOf(currentStep) }
    val progress = remember(currentStep) { Animatable(0f) }

    Row(modifier = modifier) {
        for (i in 0 until stepCount) {
            val stepProgress = when {
                i == currentStepState.intValue -> progress.value
                i > currentStepState.intValue -> 0f
                else -> 1f
            }
            LinearProgressIndicator(
                progress = { stepProgress },
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
                    .height(2.dp),
                color = selectedColor,
                trackColor = unSelectedColor,
            )
        }
    }

    LaunchedEffect(isPaused, currentStep) {
        if (isPaused) {
            progress.stop()
        } else {
            progress.snapTo(0f)
            for (i in currentStep until stepCount) {
                progress.animateTo(
                    1f,
                    animationSpec = tween(
                        durationMillis = ((1f - progress.value) * stepDuration).toInt(),
                        easing = LinearEasing
                    )
                )
                if (currentStepState.intValue + 1 <= stepCount - 1) {
                    progress.snapTo(0f)
                    currentStepState.intValue += 1
                    onStepChanged(currentStepState.intValue)
                } else {
                    onComplete()
                }
            }
        }
    }
}
