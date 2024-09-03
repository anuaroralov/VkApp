package com.example.vkapp.presentation.home.newsFeed

import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.vkapp.domain.entity.Video
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewStateWithHTMLData

@Composable
fun VideoCard(
    video: Video,
    onLinkClickListener: (String) -> Unit
) {
    video.videoUrl?.let { videoUrl ->
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.wrapContentSize(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column {
                WebViewScreen(
                    videoUrl = videoUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onLinkClickListener = onLinkClickListener
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
    modifier: Modifier,
    onLinkClickListener: (String) -> Unit
) {
    val context = LocalContext.current as ComponentActivity
    val videoHtml = remember(videoUrl) { generateVideoHtml(videoUrl) }
    val state = rememberWebViewStateWithHTMLData(videoHtml)
    val navigator = rememberWebViewNavigator()
    val chromeClient = remember { CustomChromeClient(context) }
    val webViewClient = remember { CustomWebViewClient(onLinkClickListener) }

    WebView(
        state = state,
        modifier = modifier,
        navigator = navigator,
        chromeClient = chromeClient,
        client = webViewClient,
        onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
        }
    )
}

fun generateVideoHtml(videoUrl: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <body style="margin:0; padding:0; overflow:hidden;">
            <div style="position:relative; width:100%; height:100%; padding-bottom:56.25%; overflow:hidden;">
                <iframe src="$videoUrl" style="position:absolute; top:0; left:0; width:100%; height:100%;" frameborder="0" allowfullscreen></iframe>
            </div>
        </body>
        </html>
    """.trimIndent()
}

class CustomChromeClient(
    private val activity: ComponentActivity
) : AccompanistWebChromeClient() {

    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null

    override fun onShowCustomView(view: View, callback: CustomViewCallback) {
        if (customView != null) {
            callback.onCustomViewHidden()
            return
        }
        customView = view
        customViewCallback = callback
        (activity.window.decorView as FrameLayout).addView(
            customView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    override fun onHideCustomView() {
        (activity.window.decorView as FrameLayout).removeView(customView)
        customView = null
        customViewCallback?.onCustomViewHidden()
        activity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }
}

class CustomWebViewClient(
    val onLinkClickListener: (String) -> Unit
) : AccompanistWebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        request?.url?.toString()?.let { url ->
            onLinkClickListener(url)

            return true
        }
        return false
    }
}

