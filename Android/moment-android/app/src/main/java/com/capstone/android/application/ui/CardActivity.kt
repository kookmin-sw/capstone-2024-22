package com.capstone.android.application.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class CardActivity:ComponentActivity() {
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            navController = rememberNavController()
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate("detail")
                    },
                    text = "카드뷰"
                )

                NavHost(
                    navController,
                    startDestination = "main",
                ) {
                    composable("detail"){
                        cardDetail()
                    }

                }
            }
        }

    }

    @Composable
    fun cardDetail(){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "카드뷰 상세")
        }
    }
}