package com.uts.remedy.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uts.remedy.R
import com.uts.remedy.models.MedicineViewModel
import com.uts.remedy.screens.Daily_update_Screen


@Composable
fun MenuItem(name: String, id: Int,onClick: () -> Unit) {
    val viewModel: MedicineViewModel = viewModel()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier
                .size(50.dp)
                .clickable(enabled = true)  {
                    onClick()
                           },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id),
                contentDescription = "Notifications",
                tint = Color(android.graphics.Color.parseColor("#199A8E"))
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 14.sp,
            color = Color(android.graphics.Color.parseColor("#A1A8B0"))
        )
    }
}


@Composable
fun Menu(modifier: Modifier,navController: NavController) {
    Log.d("555a","oki")
    Row(
        modifier = modifier
            .fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        MenuItem(name = "Medicine", id = R.drawable.pharmacy){

            Log.d("555a","oke")

        }
        MenuItem(name = "Tracking", id = R.drawable.document){
            Log.d("555a","oko")
            navController.navigate("daily")
        }
        MenuItem(name = "History", id = R.drawable.history){
            Log.d("555a","oke")
            navController.navigate("image")
        }

    }
}



@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    //Menu(modifier = Modifier)
}