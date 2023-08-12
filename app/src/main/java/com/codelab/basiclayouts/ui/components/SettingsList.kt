package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codelab.basiclayouts.ui.screens.settingsMenuItems

@Composable
fun SettingsList(
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier
    ) {
        items(settingsMenuItems) { item ->
            SettingCategoryDisplay(
                icon = item.icon,
                title = item.title,
                description = item.description
            )
        }

    }
}