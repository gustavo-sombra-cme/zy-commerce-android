package com.zippyyum.commerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zippyyum.commerce.core.designsystem.ZyCommerceTheme
import com.zippyyum.commerce.feature.auth.RegisterRoute
import com.zippyyum.commerce.feature.auth.SignInRoute
import com.zippyyum.commerce.feature.auth.SplashRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ROUTE_SPLASH = "splash"
private const val ROUTE_REGISTER = "register"
private const val ROUTE_SIGN_IN = "sign-in"
private const val ROUTE_AUTHENTICATED_HOME = "authenticated-home"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZyCommerceTheme {
                ZyCommerceApp()
            }
        }
    }
}

@Composable
fun ZyCommerceApp(navController: NavHostController = rememberNavController()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_SPLASH,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(ROUTE_SPLASH) {
                SplashRoute(
                    onAuthenticated = {
                        navController.navigate(ROUTE_AUTHENTICATED_HOME) {
                            popUpTo(ROUTE_SPLASH) { inclusive = true }
                        }
                    },
                    onSignInRequired = {
                        navController.navigate(ROUTE_SIGN_IN) {
                            popUpTo(ROUTE_SPLASH) { inclusive = true }
                        }
                    },
                )
            }
            composable(ROUTE_SIGN_IN) {
                SignInRoute(
                    onSignInComplete = {
                        scope.launch { snackbarHostState.showSnackbar("Signed in successfully.") }
                        navController.navigate(ROUTE_AUTHENTICATED_HOME) {
                            popUpTo(ROUTE_SIGN_IN) { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate(ROUTE_REGISTER) },
                )
            }
            composable(ROUTE_REGISTER) {
                RegisterRoute(
                    onRegistrationComplete = {
                        scope.launch { snackbarHostState.showSnackbar("Account created. Sign in is ready.") }
                        navController.navigate(ROUTE_SIGN_IN) {
                            popUpTo(ROUTE_SIGN_IN) { inclusive = true }
                        }
                    },
                    onSignInClick = {
                        navController.navigate(ROUTE_SIGN_IN) {
                            popUpTo(ROUTE_SIGN_IN) { inclusive = true }
                        }
                    },
                )
            }
            composable(ROUTE_AUTHENTICATED_HOME) {
                AuthenticatedHomeScreen()
            }
        }
    }
}

@Composable
private fun AuthenticatedHomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "You are signed in. Protected ZY-Commerce actions are now available to the authenticated app flow.",
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}
