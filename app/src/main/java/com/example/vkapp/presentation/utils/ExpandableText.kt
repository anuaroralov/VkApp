package com.example.vkapp.presentation.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableText(
    text: String,
    isExpanded: Boolean,
    onLinkClickListener: (String) -> Unit,
    onExpandClick: () -> Unit,
    tint: Color, size: TextUnit
) {
    val maxLines = if (isExpanded) Int.MAX_VALUE else 3
    val showMoreText = if (isExpanded) "Show less" else "Show more"

    val annotatedString = buildAnnotatedString {
        val urlPattern = Regex("(https?://[\\w\\-]+(\\.[\\w\\-]+)+[/#?]?.*)")
        var lastMatchEnd = 0

        urlPattern.findAll(text).forEach { result ->
            append(text.substring(lastMatchEnd, result.range.first))
            pushStringAnnotation(tag = "URL", annotation = result.value)
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(result.value)
            }
            pop()
            lastMatchEnd = result.range.last + 1
        }

        append(text.substring(lastMatchEnd, text.length))
    }

    Column {
        ClickableText(
            text = annotatedString,
            style = TextStyle(color = tint, fontSize = size),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onClick = { offset ->
                annotatedString.getStringAnnotations(start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        if (annotation.tag == "URL") {
                            onLinkClickListener(annotation.item)
                        }
                    }
            }
        )
        if (text.length > 100) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = showMoreText,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable(onClick = onExpandClick)
                        .padding(top = 4.dp)
                )
            }
        }
    }
}