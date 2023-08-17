package com.inaki.a20230816_inakilizarraga_nycschools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inaki.a20230816_inakilizarraga_nycschools.ui.theme.NYCSchoolsTheme
import com.inaki.a20230816_inakilizarraga_nycschools.utils.NavDir
import com.inaki.a20230816_inakilizarraga_nycschools.view.DetailsScreen
import com.inaki.a20230816_inakilizarraga_nycschools.view.MainScreen
import com.inaki.a20230816_inakilizarraga_nycschools.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NYCSchoolsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: MainViewModel = hiltViewModel()
                    NavHost(navController = navController, startDestination = NavDir.Main().route) {
                        composable(route = NavDir.Main().route) {
                            MainScreen(viewModel, navController)
                        }
                        composable(route = NavDir.Details().route) {
                            DetailsScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NYCSchoolsTheme {
        // MainScreen()
    }
}