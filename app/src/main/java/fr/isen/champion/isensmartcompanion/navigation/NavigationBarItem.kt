package fr.isen.champion.isensmartcompanion.navigation

import fr.isen.champion.isensmartcompanion.R

sealed class NavigationBarItem(val route: String, val labelResId: Int) {
    object Home : NavigationBarItem("home", R.string.navigation_bar_label_assistant)
    object Events : NavigationBarItem("events", R.string.navigation_bar_label_event)
    object History : NavigationBarItem("history", R.string.navigation_bar_label_history)
    object Agenda : NavigationBarItem("agenda", R.string.navigation_bar_label_agenda)
}
