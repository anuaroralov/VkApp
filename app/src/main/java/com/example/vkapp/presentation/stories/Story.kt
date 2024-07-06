package com.example.vkapp.presentation.stories

import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.vkapp.R
import kotlin.math.max
import kotlin.math.min

@Composable
fun InstagramStory() {
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
            Uri.parse("android.resource://your.package.name/raw/video1"),
            Uri.parse("android.resource://your.package.name/raw/video2")
        )
    }

    val stepCount = images.size + videos.size
    val currentStep = remember { mutableIntStateOf(0) }
    val isPaused = remember { mutableStateOf(false) }

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
        } else {
            val context = LocalContext.current
            val player = remember {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(videos[currentStep.intValue - images.size]))
                    prepare()
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    player.release()
                }
            }

            AndroidView(
                factory = { PlayerView(context).apply { this.player = player } },
                modifier = mediaModifier
            )
        }

        InstagramProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            stepCount = stepCount,
            stepDuration = if (currentStep.intValue < images.size) 2000 else 5000,
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
