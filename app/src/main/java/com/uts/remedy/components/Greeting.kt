package com.uts.remedy.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.uts.remedy.R
import java.time.LocalTime

fun getTimeOfDay(): String {
    val currentTime = LocalTime.now()
    return when {
        currentTime.isBefore(LocalTime.NOON) -> "Morning"
        currentTime.isBefore(LocalTime.of(17, 0)) -> "Afternoon"
        else -> "Evening"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = "${getTimeOfDay()}, ",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.notification),
            contentDescription = "Notifications",
            tint = Color(android.graphics.Color.parseColor("#101623"))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting("Azalea", modifier = Modifier.fillMaxWidth())
}