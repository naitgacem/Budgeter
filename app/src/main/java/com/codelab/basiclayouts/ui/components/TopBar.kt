package com.codelab.basiclayouts.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: () -> Unit) {

   TopAppBar (
       title = {
           Text(
               text = "budgeter",
               textAlign = TextAlign.Center
           )
               },
       navigationIcon = {
           Icon(
               imageVector = Icons.Filled.Person,
               contentDescription = null
           )
       },
       actions = {
           IconButton(onClick = navController) {
               Icon(
                   imageVector = Icons.Filled.Settings,
                   contentDescription = "Localized description"
               )
           }
       }
   )
}