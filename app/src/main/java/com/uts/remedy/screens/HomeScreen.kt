package com.uts.remedy.screens

import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uts.remedy.auth
import com.uts.remedy.components.Greeting
import com.uts.remedy.components.HomeBanner
import com.uts.remedy.components.MedicineTrackScreen
import com.uts.remedy.components.Menu
import com.uts.remedy.components.SearchBar
import java.util.Locale
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    val currentUser = auth.currentUser
        ?.email
        .toString()
        .replace("@gmail.com", "")
        .capitalize(Locale.getDefault())

    Column(
        modifier = Modifier.padding(top = 48.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp) // Set space between items
    ) {
        Greeting(
            name = currentUser,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(22.dp))
        SearchBar()
        Menu(modifier = Modifier,navController = navController)
        HomeBanner()
        MedicineTrackScreen(navController = navController)

    }
}

//@Preview
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen(navController = NavController())
//}
