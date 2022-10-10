package com.example.cleanarchitecturenoteapp.feature_note.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cleanarchitecturenoteapp.core.utill.TestTags
import com.example.cleanarchitecturenoteapp.di.AppModule
import com.example.cleanarchitecturenoteapp.feature_note.domain.util.Screen
import com.example.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.example.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesScreen
import com.example.cleanarchitecturenoteapp.ui.theme.CleanArchitectureNoteAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {

    @get: Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            CleanArchitectureNoteAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController, startDestination = Screen.NotesScreen.route
                ) {
                    composable(
                        route = Screen.NotesScreen.route
                    ) {
                        NotesScreen(navController = navController)
                    }
                    composable(route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(navArgument(name = "noteId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }, navArgument(name = "noteColor") {
                            type = NavType.IntType
                            defaultValue = -1
                        })
                    ) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(navController = navController, noteColor = color)
                    }
                }
            }
        }
    }

    @Test
    fun performAddAndEdit_SaveItem() {
        // Main Note screen perform click to add note
        composeRule.onNodeWithContentDescription("Add note").performClick()
        // New Edit Screen perform add text to fields and save
        composeRule.onNodeWithTag(TestTags.TITLE_SECTION).performTextInput("title_input")
        composeRule.onNodeWithTag(TestTags.CONTENT_SECTION).performTextInput("content_input")
        composeRule.onNodeWithContentDescription("Save note").performClick()
        // Main Note screen. display the entered items
        composeRule.onNodeWithText("title_input").assertIsDisplayed()
        composeRule.onNodeWithText("title_input").performClick()
        // New Edit Screen for updating our saved item
        composeRule.onNodeWithText("title_input").assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.TITLE_SECTION).performTextInput("New updated title")
        composeRule.onNodeWithText("content_input").assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.CONTENT_SECTION).performTextInput("New updated content")
        composeRule.onNodeWithContentDescription("Save note").performClick()
        // Main Note screen

    }

    @Test
    fun performNewNotes_orderByTitleDescending() {
        for (i in 0..2) {
            // Main Note screen perform click to add note
            composeRule.onNodeWithContentDescription("Add note").performClick()
            // New Edit Screen perform add text to fields and save
            composeRule.onNodeWithTag(TestTags.TITLE_SECTION).performTextInput(i.toString())
            composeRule.onNodeWithTag(TestTags.CONTENT_SECTION).performTextInput(i.toString())
            composeRule.onNodeWithContentDescription("Save note").performClick()
        }

        // We assert that our views are added with the added test
        composeRule.onNodeWithText("0").assertIsDisplayed()
        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()

        // Change our order in the order section with title and descending order
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithContentDescription("Title").performClick()
        composeRule.onNodeWithContentDescription("Descending").performClick()
        composeRule.onNodeWithContentDescription("Sort").performClick()

        // We get back to our main note screen and the expected order is title descending
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0].assertTextContains("2")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1].assertTextContains("1")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2].assertTextContains("0")
    }

}