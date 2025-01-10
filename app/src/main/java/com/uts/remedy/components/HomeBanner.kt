package com.uts.remedy.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uts.remedy.R

@Composable
fun HomeBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(135.dp)
            .clip(RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(android.graphics.Color.parseColor("#E8F3F1")))
                .padding(28.dp)
        ) {
            Text(
                text = "Sudahkah Kamu",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "minum Vitamin hari ini?",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Button(
                modifier = Modifier.offset((-2).dp, 8.dp),
                onClick = {},
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF008080)
                )
            ) {
                Text(
                    text = "Check Jadwal",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(0.dp, (-2).dp)
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.banner_image),
            contentDescription = "Banner",
            modifier = Modifier.fillMaxSize().offset(128.dp, 0.dp)
        )
    }
}

@Preview
@Composable
fun HomeBannerPreview() {
    HomeBanner()
}