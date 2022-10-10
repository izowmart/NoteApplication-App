package com.example.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cleanarchitecturenoteapp.core.utill.TestTags
import com.example.cleanarchitecturenoteapp.di.AppModule
import com.example.cleanarchitecturenoteapp.feature_note.domain.util.Screen
import com.example.cleanarchitecturenoteapp.feature_note.presentation.MainActivity
import com.example.cleanarchitecturenoteapp.ui.theme.CleanArchitectureNoteAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class) // tells dagger not t use this AppModule
class NotesScreenTest{

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp(){
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            CleanArchitectureNoteAppTheme{
                NavHost(navController = navController, startDestination = Screen.NotesScreen.route){
                    composable(
                        route = Screen.NotesScreen.route
                    ) {
                        NotesScreen(navController = navController)
                    }

                }
            }
        }

    }

    @Test
    fun toggleOrderSectionOnClick_isVisible(){
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertExists()
    }

}