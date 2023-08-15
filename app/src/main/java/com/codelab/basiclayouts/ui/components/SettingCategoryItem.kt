package com.codelab.basiclayouts.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.ui.theme.typography

@Composable
fun SettingCategoryDisplay(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String
    ){
        Row(
        modifier = modifier
            .padding(vertical = 2.dp)
            .padding(all = 8.dp)
            .fillMaxWidth()
            .height(72.dp)
            ,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = modifier
                .size(width = 40.dp, height = 40.dp)
                .padding(all = 8.dp)
        )
        Column {
            Text(
                text = title,
                textAlign = TextAlign.Left,

                style = typography.h2 ,
                modifier = modifier
                    .padding(horizontal = 8.dp)

            )
            Text(
                modifier = modifier
                    .padding(horizontal = 8.dp),
                text = description,
                style = typography.caption
            )
        }
    }
}

