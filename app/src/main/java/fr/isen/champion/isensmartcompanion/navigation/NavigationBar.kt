package fr.isen.champion.isensmartcompanion.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import fr.isen.champion.isensmartcompanion.ui.AgendaScreen
import fr.isen.champion.isensmartcompanion.ui.AssistantScreen
import fr.isen.champion.isensmartcompanion.ui.EventsScreen
import fr.isen.champion.isensmartcompanion.ui.HistoryScreen

@Composable
fun NavigationBar() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationBarItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationBarItem.Home.route) {
                AssistantScreen()
            }
            composable(NavigationBarItem.Events.route) {
                EventsScreen()
            }
            composable(NavigationBarItem.History.route) {
                HistoryScreen()
            }
            composable(NavigationBarItem.Agenda.route) {
                AgendaScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationBarItem.Home,
        NavigationBarItem.Events,
        NavigationBarItem.History,
        NavigationBarItem.Agenda
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            val icon = when (item) {
                NavigationBarItem.Home -> Icons.Default.Home
                NavigationBarItem.Events -> Icons.Default.Notifications
                NavigationBarItem.History -> Icons.Default.Refresh
                NavigationBarItem.Agenda -> Icons.Default.DateRange
            }

            val labelText = stringResource(item.labelResId)

            NavigationBarItem(
                icon = { Icon(
                    imageVector = icon,
                    contentDescription = labelText
                ) },
                label = { Text(labelText) },
                selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination to avoid building a large back stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}
