package com.ke.music.fold

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.ke.music.fold.ui.album_detail.AlbumDetailRoute
import com.ke.music.fold.ui.artist_detail.ArtistDetailRoute
import com.ke.music.fold.ui.comment.CommentRoute
import com.ke.music.fold.ui.login.LoginRoute
import com.ke.music.fold.ui.mv_detail.MvDetailRoute
import com.ke.music.fold.ui.playlist_detail.PlaylistDetailRoute
import com.ke.music.fold.ui.splash.SplashRoute
import com.ke.music.fold.ui.user_playlists.UserPlaylistsRoute

@Composable
fun MyApp(windowSize: WindowSizeClass, navigationController: NavHostController) {


    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactNavHost(navigationController, windowSize)
        }

        WindowWidthSizeClass.Medium -> {
            MediumNavHost(navigationController, windowSize)
        }

        WindowWidthSizeClass.Expanded -> {
            ExpandedNavHost(navigationController, windowSize)
        }

    }

}


@Composable
private fun MediumNavigationBar(navHostController: NavHostController) {
    NavigationRail {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        listOf(
            MainScreenRoute.Home,
            MainScreenRoute.Message,
            MainScreenRoute.Playlist,
            MainScreenRoute.Mine
        ).forEach { item ->
            NavigationRailItem(selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navHostController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }, label = {
                    Text(text = item.label)
                }, icon = {
                    Icon(imageVector = item.icon, contentDescription = null)
                })

        }

    }
}


@Composable
private fun CompactNavigationBar(navHostController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        listOf(
            MainScreenRoute.Home,
            MainScreenRoute.Message,
            MainScreenRoute.Playlist,
            MainScreenRoute.Mine
        ).forEach { item ->
            NavigationBarItem(selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navHostController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }, label = {
                    Text(text = item.label)
                }, icon = {
                    Icon(imageVector = item.icon, contentDescription = null)
                })

        }
    }
}


@Composable
private fun ExpandedNavigationBar(navHostController: NavHostController) {
    PermanentDrawerSheet(
        modifier = Modifier
            .sizeIn(minWidth = 160.dp, maxWidth = 240.dp),
        drawerContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
    ) {

//        Text(
//            text = "克音乐",
//            style = MaterialTheme.typography.headlineMedium.copy(
//                color = MaterialTheme.colorScheme.primary
//            ),
//            modifier = Modifier.padding(16.dp)
//        )

        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        listOf(
            MainScreenRoute.Home,
            MainScreenRoute.Message,
            MainScreenRoute.Playlist,
            MainScreenRoute.Mine
        ).forEach { item ->
            NavigationDrawerItem(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navHostController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true


                    }
                }, label = {
                    Text(text = item.label)
                }, icon = {
                    Icon(imageVector = item.icon, contentDescription = null)
                })

        }
    }
}


@Composable
private fun CompactNavHost(
    navigationController: NavHostController,
    windowSize: WindowSizeClass,
) {
    var showNavigationBar by remember { mutableStateOf(true) }

    if (showNavigationBar) {
        Scaffold(
            bottomBar = {
                CompactNavigationBar(navHostController = navigationController)
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                NavigationHostWrapper(navigationController, windowSize) {
                    showNavigationBar = it
                }
            }
        }
    } else {
        NavigationHostWrapper(navigationController, windowSize) {
            showNavigationBar = it
        }
    }


}

@Composable
private fun NavigationHostWrapper(
    navigationController: NavHostController,
    windowSize: WindowSizeClass,
    toggleNavigationBarVisible: (Boolean) -> Unit = {},
) {
    NavHost(navController = navigationController,
        startDestination = ScreenRoute.Splash.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None

        },
        popExitTransition = {
            ExitTransition.None
        }) {
        navigationCommonSettings(this, navigationController, windowSize, toggleNavigationBarVisible)
    }
}

private fun navigationCommonSettings(
    builder: NavGraphBuilder,
    navigationController: NavHostController,
    windowSizeClass: WindowSizeClass,
    toggleNavigationBarVisible: (Boolean) -> Unit = {},
) {

    builder.composable(ScreenRoute.Splash.route) {
        toggleNavigationBarVisible(false)
        SplashRoute { hasLogin ->
            if (hasLogin) {
                navigationController.navigate(ScreenRoute.Main.route) {
                    popUpTo(ScreenRoute.Splash.route) {
                        inclusive = true
                    }
                }
            } else {
                navigationController.navigate(ScreenRoute.Login.route) {
                    popUpTo(ScreenRoute.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    builder.composable(ScreenRoute.Login.route) {
        toggleNavigationBarVisible(false)

        LoginRoute {
            navigationController.navigate(ScreenRoute.Main.route) {
                popUpTo(ScreenRoute.Login.route) {
                    inclusive = true
                }
            }
        }
    }

    builder.navigation(
        startDestination = MainScreenRoute.Playlist.route,
        route = ScreenRoute.Main.route
    ) {
        toggleNavigationBarVisible(true)


        composable(MainScreenRoute.Home.route) {

        }

        composable(MainScreenRoute.Message.route) {

        }

        navigation(
            startDestination = MainRoute.UserPlaylists.route,
            route = MainScreenRoute.Playlist.route
        ) {
            composable(MainRoute.UserPlaylists.route) {
                UserPlaylistsRoute(windowSizeClass = windowSizeClass, onPlaylistItemClick = {
                    navigationController.navigate("/playlist/${it}")
                })
            }

            composable(MainRoute.PlaylistDetail.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )) {
                PlaylistDetailRoute(windowSizeClass = windowSizeClass, closeDetailScreen = {
                    navigationController.popBackStack()
                })

            }

            composable(MainRoute.ArtistDetail.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )) {
                ArtistDetailRoute(windowSizeClass = windowSizeClass) {
                    navigationController.popBackStack()
                }

            }

            composable(MainRoute.MvDetail.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )) {
                MvDetailRoute(windowSizeClass = windowSizeClass) {
                    navigationController.popBackStack()
                }
            }

            composable(MainRoute.AlbumDetail.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )) {
                AlbumDetailRoute(windowSizeClass = windowSizeClass) {
                    navigationController.popBackStack()
                }
            }


            composable(MainRoute.Comment.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                },
                navArgument("type") {
                    type = NavType.IntType
                }
            )) {
                CommentRoute(windowSizeClass = windowSizeClass) {
                    navigationController.popBackStack()
                }
            }
        }




        composable(MainScreenRoute.Mine.route) {

        }


    }

}

@Composable
private fun MediumNavHost(navigationController: NavHostController, windowSize: WindowSizeClass) {


    var showNavigationBar by remember { mutableStateOf(true) }

    if (showNavigationBar) {

        Row(modifier = Modifier.fillMaxSize()) {
            MediumNavigationBar(navigationController)
            // Other content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                NavigationHostWrapper(
                    navigationController = navigationController,
                    windowSize = windowSize
                ) {
                    showNavigationBar = it
                }
            }
        }
    } else {
        NavigationHostWrapper(
            navigationController = navigationController,
            windowSize = windowSize
        ) {
            showNavigationBar = it
        }

    }
}

@Composable
private fun ExpandedNavHost(navigationController: NavHostController, windowSize: WindowSizeClass) {
    var showNavigationBar by remember { mutableStateOf(true) }

    if (showNavigationBar) {

        PermanentNavigationDrawer(
            drawerContent = {
                ExpandedNavigationBar(navigationController)
            },
            content = {
                NavigationHostWrapper(
                    navigationController = navigationController,
                    windowSize = windowSize
                ) {
                    showNavigationBar = it
                }
            }
        )
    } else {
        NavigationHostWrapper(
            navigationController = navigationController,
            windowSize = windowSize
        ) {
            showNavigationBar = it
        }


    }

}


private sealed class ScreenRoute(val route: String) {
    data object Splash : ScreenRoute("/app/splash")

    data object Login : ScreenRoute("/app/login")

    data object Main : ScreenRoute("/app/main")
}

internal sealed class MainRoute(val route: String) {

    data object UserPlaylists : MainRoute("/main/playlists/index")

    data object PlaylistDetail : MainRoute("/playlist/{id}") {
        fun createRoute(id: Long): String {
            return "/playlist/$id"
        }
    }

    data object ArtistDetail : MainRoute("/artist/{id}") {
        fun createRoute(id: Long): String {
            return "/artist/$id"
        }
    }

    data object MvDetail : MainRoute("/mv/{id}") {
        fun createRoute(id: Long): String {
            return "/mv/$id"
        }
    }

    data object AlbumDetail : MainRoute("/album/{id}") {
        fun createRoute(id: Long): String {
            return "/album/$id"
        }
    }

    data object Comment : MainRoute("/comment?id={id}&type={type}") {

        fun createRoute(id: Long, type: Int): String {
            return "/comment?id=$id&type=$type"
        }
    }
}

sealed class MainScreenRoute(val route: String, val label: String, val icon: ImageVector) {

    data object Home : MainScreenRoute("/main/home", "首页", Icons.Default.Home)

    data object Message : MainScreenRoute("/main/message", "消息", Icons.Default.Message)

    data object Playlist : MainScreenRoute("/main/playlist", "歌单", Icons.Default.LibraryMusic)

    data object Mine : MainScreenRoute("/main/mine", "我的", Icons.Default.AccountCircle)

}