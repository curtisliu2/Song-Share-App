package com.example.cs3200firebasestarter.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.example.cs3200firebasestarter.ui.repositories.UserRepository
import com.example.cs3200firebasestarter.ui.screens.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {

    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("SongShare", modifier = Modifier.padding(16.dp))
                Divider()

                NavigationDrawerItem(label = { Text(text = "Feed")},
                    selected = false,
                    onClick = {
                        navController.navigate(Routes.home.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                        // Close the drawer after navigation
                        scope.launch {
                            drawerState.close()
                        }
                    })

                NavigationDrawerItem(label = { Text(text = "Friends")},
                    selected = false,
                    onClick = {
                        navController.navigate(Routes.friendsList.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                        // Close the drawer after navigation
                        scope.launch {
                            drawerState.close()
                        }
                    })

                NavigationDrawerItem(label = { Text(text = "Spotify Connect")},
                    selected = false,
                    onClick = {
                        navController.navigate(Routes.spotifyConnect.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                        // Close the drawer after navigation
                        scope.launch {
                            drawerState.close()
                        }
                    })


                NavigationDrawerItem(label = { Text(text = "Song Search (Experimental)")},
                    selected = false,
                    onClick = {
                        navController.navigate(Routes.songSearch.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                        // Close the drawer after navigation
                        scope.launch {
                            drawerState.close()
                        }
                    })

                NavigationDrawerItem(
                    label = { Text(text = "Logout") },
                    selected = false,
                    onClick = {
                        UserRepository.logout()
                        navController.navigate(Routes.launchNavigation.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                        scope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    }
                )
                // ...other drawer items
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (currentDestination?.hierarchy?.none { it.route == Routes.launchNavigation.route || it.route == Routes.splashScreen.route } == true) {
                    TopAppBar(
                        title = { Text(text = "SongShare")},
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu button")
                            }
                        }
                    )
                }
            },
            floatingActionButton = {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (currentDestination?.hierarchy?.none { it.route == Routes.launchNavigation.route || it.route == Routes.splashScreen.route } == true) {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("friendAdd")
                            },
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(text = "Add Friend",
                                modifier = Modifier.padding(12.dp))
                        }
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("createPost")
                            },
                        ){
                            Text(
                                text = "Create Post",
                                modifier = Modifier.padding(10.dp))
                        }
                    }
                }
            }

        ) {

            NavHost(
                navController = navController,
                startDestination = Routes.splashScreen.route,
                modifier = Modifier.padding(paddingValues = it)
            ) {
                navigation(route = Routes.launchNavigation.route, startDestination = Routes.launch.route) {
                    composable(route = Routes.launch.route) { LaunchScreen(navController) }
                    composable(route = Routes.signIn.route) { SignInScreen(navController) }
                    composable(route = Routes.signUp.route) { SignUpScreen(navController) }
                }
                navigation(route = Routes.appNavigation.route, startDestination = Routes.home.route) {
                    composable(route = Routes.home.route) { HomeScreen(navController) }
                    composable(route = Routes.spotifyConnect.route) { SpotifyConnectScreen(navController) }
                    composable(route = Routes.friendsList.route) { FriendsScreen(navController) }

                }
                composable(route = Routes.splashScreen.route) { SplashScreen(navController) }
                composable(Routes.addFriend.route) {
                    FriendAddScreen(navController)
                }
                composable(Routes.postCreation.route) {
                    PostCreationScreen(navController)
                }
                composable(route = Routes.splashScreen.route) { SplashScreen(navController) }
            }
        }
    }
}