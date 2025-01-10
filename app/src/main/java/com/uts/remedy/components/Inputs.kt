package com.uts.remedy.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.uts.remedy.R

@Composable
fun BaseTextField() {
    TextField(
        value = TextFieldValue(""),
        onValueChange = {},
        placeholder = {
            Text(text = "Search medicine, articles...")
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search Icon",
                modifier = Modifier.size(24.dp).offset(6.dp, 0.dp),
                tint = Color(android.graphics.Color.parseColor("#ADADAD"))
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.qr_scanner),
                contentDescription = "Search Icon",
                modifier = Modifier.size(24.dp).offset((-4).dp, 0.dp),
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(100.dp)),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color(android.graphics.Color.parseColor("#ADADAD")),
            unfocusedPlaceholderColor = Color(android.graphics.Color.parseColor("#ADADAD")),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color(android.graphics.Color.parseColor("#FBFBFB")),
            focusedContainerColor = Color(android.graphics.Color.parseColor("#FBFBFB")),
        )
    )
}
