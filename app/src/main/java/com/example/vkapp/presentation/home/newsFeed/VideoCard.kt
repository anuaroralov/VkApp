package com.example.vkapp.presentation.home.newsFeed

import android.app.Activity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkapp.domain.Video
import com.example.vkapp.presentation.main.MainActivity

@Composable
fun VideoCard(
    video: Video,
    videoViewModel: VideoViewModel = viewModel()
) {
    val isFullScreen by videoViewModel.isFullScreen.observeAsState(false)

    if (isFullScreen) {
        FullScreenWebView(
            videoUrl = video.videoUrl,
            onExitFullScreen = { videoViewModel.exitFullScreen() },
            videoViewModel = videoViewModel
        )
    } else {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.wrapContentSize(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column {
                WebViewScreen(
                    videoUrl = video.videoUrl,
                    onFullScreenRequested = { videoViewModel.enterFullScreen() },
                    videoViewModel = videoViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = video.title,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${video.views} views",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun WebViewScreen(
    videoUrl: String,
    onFullScreenRequested: () -> Unit,
    videoViewModel: VideoViewModel,
    modifier: Modifier
) {
    val context = LocalContext.current as MainActivity
    val videoHtml = """
    <!DOCTYPE html>
    <html>
    <body style="margin:0; padding:0; overflow:hidden;">
        <div style="position:relative; width:100%; height:100%; padding-bottom:56.25%; overflow:hidden;">
            <iframe src="$videoUrl" style="position:absolute; top:0; left:0; width:100%; height:100%;" frameborder="0" allowfullscreen></iframe>
        </div>
    </body>
    </html>
    """.trimIndent()

    AndroidView(
        modifier = modifier,
        factory = {
            WebView(context).apply {
                videoViewModel.webView = this
                webViewClient = WebViewClient()
                webChromeClient = object : WebChromeClient() {
                    private var customViewCallback: CustomViewCallback? = null
                    private var fullscreenView: View? = null

                    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                        if (fullscreenView != null) {
                            callback?.onCustomViewHidden()
                            return
                        }
                        fullscreenView = view
                        customViewCallback = callback
                        context.apply {
                            setContentView(fullscreenView)
                            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                            enterFullScreen()
                        }
                        onFullScreenRequested()
                    }

                    override fun onHideCustomView() {
                        context.apply {
                            setContentView(android.R.id.content)
                            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                            exitFullScreen()
                        }
                        fullscreenView = null
                        customViewCallback?.onCustomViewHidden()
                        customViewCallback = null
                    }
                }
                settings.javaScriptEnabled = true
                loadDataWithBaseURL(null, videoHtml, "text/html", "utf-8", null)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(null, videoHtml, "text/html", "utf-8", null)
        }
    )
}

@Composable
fun FullScreenWebView(
    videoUrl: String,
    onExitFullScreen: () -> Unit,
    videoViewModel: VideoViewModel
) {
    val context = LocalContext.current as MainActivity
    val videoHtml = """
    <!DOCTYPE html>
    <html>
    <body style="margin:0; padding:0; overflow:hidden;">
        <div style="position:relative; width:100%; height:100%; padding-bottom:56.25%; overflow:hidden;">
            <iframe src="$videoUrl&js_api=1" style="position:absolute; top:0; left:0; width:100%; height:100%;" frameborder="0" allowfullscreen></iframe>
        </div>
    </body>
    </html>
    """.trimIndent()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = {
                WebView(context).apply {
                    videoViewModel.webView = this
                    webViewClient = WebViewClient()
                    webChromeClient = object : WebChromeClient() {
                        private var customViewCallback: CustomViewCallback? = null
                        private var fullscreenView: View? = null

                        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                            if (fullscreenView != null) {
                                callback?.onCustomViewHidden()
                                return
                            }
                            fullscreenView = view
                            customViewCallback = callback
                            context.apply {
                                setContentView(fullscreenView)
                                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                                enterFullScreen()
                            }
                        }

                        override fun onHideCustomView() {
                            context.apply {
                                setContentView(android.R.id.content)
                                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                                exitFullScreen()
                            }
                            fullscreenView = null
                            customViewCallback?.onCustomViewHidden()
                            customViewCallback = null
                            onExitFullScreen()
                        }
                    }
                    settings.javaScriptEnabled = true
                    loadDataWithBaseURL(null, videoHtml, "text/html", "utf-8", null)
                }
            },
            update = { webView ->
                webView.loadDataWithBaseURL(null, videoHtml, "text/html", "utf-8", null)
            }
        )
    }
}



