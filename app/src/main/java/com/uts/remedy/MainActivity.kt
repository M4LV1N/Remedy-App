package com.uts.remedy

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.uts.remedy.components.AllMedicineTrackScreen
import com.uts.remedy.models.AuthViewModel
import com.uts.remedy.screens.AddMedicineScreen
import com.uts.remedy.screens.GetStartedScreen
import com.uts.remedy.screens.HomeScreen
import com.uts.remedy.screens.LoginScreen
import com.uts.remedy.screens.OnboardScreen
import com.uts.remedy.screens.Daily_update_Screen
import com.uts.remedy.screens.ImageScreen
import com.uts.remedy.screens.SignUpScreen
import com.uts.remedy.ui.theme.RemedyTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)

enum class UserState {
    NO_AUTH,
    ONBOARDING,
    LOGGED_IN,
//    UNKNOWN
}

val auth = FirebaseAuth.getInstance()

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        val authVm = AuthViewModel(auth)
        val currentUser = auth.currentUser

        setContent {
            val (userState, setUserState) = remember { mutableStateOf(determineInitialUserState(currentUser)) }
            val navController = rememberNavController()

            LaunchedEffect(authVm.userLiveData.value) {
                authVm.userLiveData.value?.let {
                    setUserState(UserState.LOGGED_IN)
                    Log.d("MainActivity", "User is logged in")
                }
            }

            LaunchedEffect(userState) {
                when (userState) {
                    UserState.LOGGED_IN -> navController.navigate("home")
                    UserState.ONBOARDING -> navController.navigate("onboard")
                    UserState.NO_AUTH -> navController.navigate("login")
                }
            }

            RemedyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (userState == UserState.LOGGED_IN) TabView(getTabBarItems(), navController)
                        }
                    ) {
                        NavHost(navController = navController, startDestination = getStartDestination(userState)) {
                            composable("onboard") { OnboardScreen(navController) }
                            composable("daily") { Daily_update_Screen() }
                            composable("image") {ImageScreen() }

                            composable("get-started") { GetStartedScreen(navController) }
                            composable("login") { LoginScreen(authVm, navController, setUserState) }
                            composable("signup") { SignUpScreen(authVm, navController) }
                            composable("home") { HomeScreen(navController = navController) }
                            composable("add-medicine") { AddMedicineScreen(navController = navController) }
                            composable("all-tracks") { AllMedicineTrackScreen() }
                            composable("profile") {
                                Text("Profile",modifier = Modifier
                                    .padding(top = 50.dp)
                                    .padding(start=50.dp)
                                )
                                Button(onClick = {
                                    auth.signOut()
                                    setUserState(UserState.NO_AUTH)
                                },
                                    modifier = Modifier
                                        .padding(80.dp)
                                        .width(200.dp),
                                    ) {
                                    Text("Logout")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun determineInitialUserState(currentUser: FirebaseUser?): UserState {
        return if (currentUser != null) {
            if (hasUserOnboarded(this)) UserState.LOGGED_IN else UserState.ONBOARDING
        } else {
            UserState.NO_AUTH
        }
    }

    private fun getTabBarItems(): List<TabBarItem> {
        return listOf(
            TabBarItem("home", Icons.Filled.Home, Icons.Outlined.Home),
            TabBarItem("add-medicine", Icons.Filled.Add, Icons.Outlined.Add),
            TabBarItem("profile", Icons.Filled.Person, Icons.Outlined.Person)
        )
    }

    private fun getStartDestination(userState: UserState): String {
        return when (userState) {
            UserState.NO_AUTH -> "login"
            UserState.ONBOARDING -> "onboard"
            UserState.LOGGED_IN -> "home"
        }
    }
}

// ----------------------------------------
// This is a wrapper view that allows us to easily and cleanly
// reuse this component in any future project
@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }


    NavigationBar {
        // looping over each tab to generate the views and navigation for each item
        tabBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(tabBarItem.title)
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
            )
        }
    }
}

// This component helps to clean up the API call from our TabView above,
// but could just as easily be added inside the TabView without creating this custom component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
) {
    val iconModifier = if (title == "Add Medicine") {
        Modifier
            .size(44.dp)
            .background(Color(0xFF008080))
    } else {
        Modifier
    }
    BadgedBox(
        modifier = Modifier.clip(RoundedCornerShape(50.dp)),
        badge = { TabBarBadgeView(badgeAmount) }
    ) {
        Icon(
            imageVector = if (isSelected) {selectedIcon} else {unselectedIcon},
            contentDescription = title,
            modifier = iconModifier,
            tint =
            if (isSelected)
                if (title == "Add Medicine") Color(0xFFFFFFFF)
                else Color(0xFF008080)
            else
                if (title == "Add Medicine") {Color(0xFFFFFFFF)}
                else {MaterialTheme.colorScheme.onSurfaceVariant}
        )
    }
}

// This component helps to clean up the API call from our TabBarIconView above,
// but could just as easily be added inside the TabBarIconView without creating this custom component
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}

fun hasUserOnboarded(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("has_onboarded", false)
}

fun markUserAsOnboarded(context: Context) {
    val sharedPreferences = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("has_onboarded", true)
    editor.apply()
}
