package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FactCheckVerdict
import com.example.ui.theme.VerdictFalse
import com.example.ui.theme.VerdictPartial
import com.example.ui.theme.VerdictTrue

@Composable
fun JudgmentButtonRow(
    selectedVerdict: FactCheckVerdict?,
    onSelectVerdict: (FactCheckVerdict) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Qual é seu palpite antes da análise?"
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Verdadeiro Button
                val isTrueSelected = selectedVerdict == FactCheckVerdict.VERDADEIRO
                Button(
                    onClick = { onSelectVerdict(FactCheckVerdict.VERDADEIRO) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_predict_verdadeiro"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTrueSelected) VerdictTrue else VerdictTrue.copy(alpha = 0.12f),
                        contentColor = if (isTrueSelected) Color.White else VerdictTrue
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = if (!isTrueSelected) BorderStroke(1.dp, VerdictTrue.copy(alpha = 0.4f)) else null
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verdadeiro",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Verdadeiro",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Falso Button
                val isFalseSelected = selectedVerdict == FactCheckVerdict.FALSO
                Button(
                    onClick = { onSelectVerdict(FactCheckVerdict.FALSO) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_predict_falso"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFalseSelected) VerdictFalse else VerdictFalse.copy(alpha = 0.12f),
                        contentColor = if (isFalseSelected) Color.White else VerdictFalse
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = if (!isFalseSelected) BorderStroke(1.dp, VerdictFalse.copy(alpha = 0.4f)) else null
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Falso",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Falso",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Parcial Button
                val isPartialSelected = selectedVerdict == FactCheckVerdict.PARCIALMENTE_VERDADEIRO
                Button(
                    onClick = { onSelectVerdict(FactCheckVerdict.PARCIALMENTE_VERDADEIRO) },
                    modifier = Modifier
                        .weight(1.1f)
                        .height(44.dp)
                        .testTag("btn_predict_parcial"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPartialSelected) VerdictPartial else VerdictPartial.copy(alpha = 0.12f),
                        contentColor = if (isPartialSelected) Color.White else VerdictPartial
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = if (!isPartialSelected) BorderStroke(1.dp, VerdictPartial.copy(alpha = 0.4f)) else null
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Parcial",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Parcial",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
