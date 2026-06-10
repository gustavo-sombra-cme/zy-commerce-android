package com.zycommerce.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zycommerce.android.features.catalog.presentation.detail.ProductDetailScreen
import com.zycommerce.android.features.catalog.presentation.list.ProductListScreen

private object Routes {
    const val PRODUCT_LIST = "products"
    const val PRODUCT_DETAIL = "products/{productId}"
    fun productDetail(id: String) = "products/$id"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.PRODUCT_LIST
    ) {
        composable(Routes.PRODUCT_LIST) {
            ProductListScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Routes.productDetail(id))
                }
            )
        }

        composable(
            route = Routes.PRODUCT_DETAIL,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) {
            ProductDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
