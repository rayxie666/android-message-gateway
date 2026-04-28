package com.messagegateway.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.messagegateway.ui.screen.LogScreen
import com.messagegateway.ui.screen.RuleEditorScreen
import com.messagegateway.ui.screen.RuleListScreen
import com.messagegateway.ui.screen.SettingsScreen

object Routes {
    const val RULE_LIST = "rules"
    const val RULE_EDITOR = "rule_editor?ruleId={ruleId}"
    const val SETTINGS = "settings"
    const val LOGS = "logs"

    fun ruleEditor(ruleId: Long? = null): String {
        return if (ruleId != null) "rule_editor?ruleId=$ruleId" else "rule_editor"
    }
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.RULE_LIST) {
        composable(Routes.RULE_LIST) {
            RuleListScreen(
                onAddRule = { navController.navigate(Routes.ruleEditor()) },
                onEditRule = { ruleId -> navController.navigate(Routes.ruleEditor(ruleId)) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onOpenLogs = { navController.navigate(Routes.LOGS) }
            )
        }

        composable(
            route = Routes.RULE_EDITOR,
            arguments = listOf(
                navArgument("ruleId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val ruleId = backStackEntry.arguments?.getLong("ruleId") ?: -1L
            RuleEditorScreen(
                ruleId = if (ruleId == -1L) null else ruleId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Routes.LOGS) {
            LogScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
