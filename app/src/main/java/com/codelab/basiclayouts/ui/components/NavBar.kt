package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NavBar(modifier: Modifier = Modifier){
    NavigationBar {
        NavigationBarItem(
            modifier = modifier,
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(imageVector = Icons.Filled.Home , contentDescription = null) }
        )
        NavigationBarItem(
            modifier = modifier,
            selected = false ,
            onClick = { /*TODO*/ },
            icon = {
                Icon(imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = modifier.fillMaxSize().clip(CircleShape)
                )
                   },

        )
        NavigationBarItem(
            modifier = modifier,
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(imageVector = Icons.Filled.History , contentDescription = null) }
        )
    }
}

@Composable
@Preview
fun NavBarPreview(){
    NavBar()
}