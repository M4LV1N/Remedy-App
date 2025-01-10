package com.uts.remedy.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.uts.remedy.R
import com.uts.remedy.markUserAsOnboarded

@Composable
fun OnboardScreen(
    navController: NavHostController?
) {
    var step by remember { mutableIntStateOf(0) }

    fun handleNext() {
        when (step) {
            0 -> step = 1
            1 -> step = 2
            2 -> step = 3
        }
    }

    fun handleSkip() {
        step = 3
    }

    // effect for handling if step is 3, then navigate to get-started
    LaunchedEffect(step) {
        if (step == 3) {
            markUserAsOnboarded(
                context = navController?.context ?: return@LaunchedEffect
            )
            navController.popBackStack()
            navController.navigate("get-started")
        }
    }

    when (step) {
        0 -> StepView(
            title = "Quickly find detailed information about medication",
            image = R.drawable.onbard1,
            nextFn = ::handleNext,
            skipFn = ::handleSkip,
            currentStep = step
        )
        1 -> StepView(
            title = "Keep track of your medicine to receive reminders",
            image = R.drawable.onboard2,
            nextFn = ::handleNext,
            skipFn = ::handleSkip,
            currentStep = step
        )
        2 -> StepView(
            title = "Get the reminder by notifications",
            image = R.drawable.onboard3,
            nextFn = ::handleNext,
            skipFn = ::handleSkip,
            currentStep = step
        )
    }
}

@Composable
fun StepView(title: String, image: Int, nextFn: () -> Unit, skipFn: () -> Unit, currentStep: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Top bar
        TopAppBar(
            modifier = Modifier.padding(20.dp),
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            title = { Text("") },
            actions = {
                Text("Skip", modifier = Modifier.clickable(onClick = skipFn))
            }
        )

        // Image and description
        Image(
            painter = painterResource(id = image), // Replace with your image resource
            contentDescription = "Onboard images",
            modifier = Modifier
                .width(314.dp)
                .height(418.dp)
        )

        Box(modifier = Modifier.padding(24.dp)) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
            ) {
                Column (
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .height(226.dp)
                        .padding(16.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                            .width(230.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // step box indicator
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                            .clip(RoundedCornerShape(44.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(8.dp)
                                    .background(when (currentStep) {
                                        0 -> Color(0xFF008080)
                                        else -> Color.LightGray
                                    })
                            )
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(8.dp)
                                    .background(when (currentStep) {
                                        1 ->  Color(0xFF008080)
                                        else -> Color.LightGray
                                    })
                            )
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(8.dp)
                                    .background(when (currentStep) {
                                        2 ->  Color(0xFF008080)
                                        else -> Color.LightGray
                                    })
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next",
                            modifier = Modifier.clickable(onClick = nextFn)
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OnboardScreenPreview() {
    OnboardScreen(null)
}
