package com.uts.remedy.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.uts.remedy.models.MedicineViewModel

@Composable
fun MedicineTrackScreen(viewModel: MedicineViewModel = viewModel(), navController: NavController) {
    val medicines by viewModel.medicines.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        // title and see all
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Medicine Track",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )

            TextButton(onClick = { navController.navigate("all-tracks") }) {
                Text("See All")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            medicines.forEach { medicine ->
                MedicineCard(
                    imageRes = medicine.image, // Ensure this field exists in your Medicine data class
                    title = medicine.name,
                    subtitle = medicine.type,
                )
            }
        }
    }
}

@Composable
fun MedicineCard(imageRes: String, title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .width(118.dp)
            .background(Color(0xFFF5F5F5))
            .clickable { /* Handle click */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xE8F3F1FF)),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Log.d("img9",imageRes)
            AsyncImage(// Load image using Coil
                model = imageRes,
                contentDescription = title,
                modifier = Modifier.size(97.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    lineHeight = 1.sp,
                    maxLines = 1, // Limit to 1 line
                    overflow = TextOverflow.Ellipsis, // Add ellipsis for overflow
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 9.sp,
                    lineHeight = 1.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                AddButton()
            }
        }
    }
}

@Composable
fun AddButton() {
    Box(
        modifier = Modifier
            .background(Color(0xFF008080), RoundedCornerShape(100.dp)) // Background color and shape
            .padding(horizontal = 8.dp) // Padding inside the button
            .height(16.dp) // Set a height for the button
            .wrapContentSize()
            .offset(y = (-4).dp), // Center the content horizontally
        contentAlignment = Alignment.Center // Center content within the Box
    ) {
        Text(
            text = "+ Add", // Add "+" before "Add"
            color = Color.White,
            fontSize = 8.sp, // Adjust font size to match design
            fontWeight = FontWeight.Bold
        )
    }
}

//@Preview
//@Composable
//fun MedicineTrackScreenPreview() {
//    MedicineTrackScreen()
//}
