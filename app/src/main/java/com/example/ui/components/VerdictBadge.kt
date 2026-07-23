package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FactCheckVerdict

@Composable
fun VerdictBadge(
    verdict: FactCheckVerdict,
    modifier: Modifier = Modifier,
    large: Boolean = false
) {
    val icon = when (verdict) {
        FactCheckVerdict.VERDADEIRO -> Icons.Default.CheckCircle
        FactCheckVerdict.FALSO -> Icons.Default.Cancel
        FactCheckVerdict.PARCIALMENTE_VERDADEIRO -> Icons.Default.Warning
        FactCheckVerdict.SEM_COMPROVACAO -> Icons.Default.HelpOutline
    }

    val paddingHorizontal = if (large) 14.dp else 10.dp
    val paddingVertical = if (large) 8.dp else 4.dp
    val iconSize = if (large) 20.dp else 16.dp
    val fontSize = if (large) 15.sp else 12.sp

    Row(
        modifier = modifier
            .testTag("verdict_badge_${verdict.name.lowercase()}")
            .clip(RoundedCornerShape(24.dp))
            .background(verdict.color.copy(alpha = 0.15f))
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = verdict.label,
            tint = verdict.color,
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = verdict.label,
            color = verdict.color,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize
        )
    }
}
