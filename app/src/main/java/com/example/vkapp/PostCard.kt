package com.example.vkapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vkapp.ui.theme.VkAppTheme

@Composable
fun PostCard(){
    Card(shape= RoundedCornerShape(4.dp)
    ){
        Column(Modifier.padding(8.dp)) {
            PostHeader()
            Text(text = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa")
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                ActionButton(icon = R.drawable.baseline_favorite_border_24, count = "4")
                Spacer(modifier = Modifier.width(2.dp))
                ActionButton(icon = R.drawable.baseline_comment_24, count = "88")
                Spacer(modifier = Modifier.width(2.dp))
                ActionButton(icon = R.drawable.baseline_send_24, count = "88")
                Spacer(Modifier.weight(1f))
                Row{
                    Icon(painter = painterResource(id = R.drawable.baseline_visibility_24), contentDescription = "22k")
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "22k")
                }
            }
        }
    }
}

@Composable
fun PostHeader(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)){
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Column (modifier = Modifier
            .weight(1f)){
            Text(text="/dev/null")
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "14:00")
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null)
    }
}

@Composable
fun ActionButton(icon: Int, count: String) {
    Surface(
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(4.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(painter = painterResource(id = icon), contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = count)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostCardLight(){
    VkAppTheme(darkTheme = false) {
        PostCard()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostCardDark(){
    VkAppTheme(darkTheme = true) {
        PostCard()
    }
}