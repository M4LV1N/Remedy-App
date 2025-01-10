package com.uts.remedy.screens

import com.uts.remedy.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun GetStartedScreen(
    navController: NavController?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Image
        Image(
            painter = painterResource(id = R.drawable.remedy_logo),
            contentDescription = "Remedy logo",
            modifier = Modifier.size(120.dp)
        )

        // Title
        Text(
            text = "Let's get started!",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Subtitle
        Text(
            text = "Login to enjoy the features we've provided, and stay healthy!",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Login button
        Button(
            onClick = { navController?.navigate("login") },
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text("Login")
        }

        // Sign up button
        OutlinedButton(
            onClick = { navController?.navigate("signup") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Sign Up", color = MaterialTheme.colorScheme.primary)
        }
    }
}
