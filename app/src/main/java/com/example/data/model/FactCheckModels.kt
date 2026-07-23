package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.VerdictFalse
import com.example.ui.theme.VerdictFalseLight
import com.example.ui.theme.VerdictPartial
import com.example.ui.theme.VerdictPartialLight
import com.example.ui.theme.VerdictTrue
import com.example.ui.theme.VerdictTrueLight
import com.example.ui.theme.VerdictUnverified
import com.example.ui.theme.VerdictUnverifiedLight

enum class FactCheckVerdict(
    val label: String,
    val description: String,
    val color: Color,
    val containerColor: Color
) {
    VERDADEIRO(
        label = "Verdadeiro",
        description = "A informação foi verificada e confirmada por fontes e relatórios oficiais.",
        color = VerdictTrue,
        containerColor = VerdictTrueLight
    ),
    FALSO(
        label = "Falso",
        description = "A afirmação é completamente falsa ou foi inventada/desmentida oficialmente.",
        color = VerdictFalse,
        containerColor = VerdictFalseLight
    ),
    PARCIALMENTE_VERDADEIRO(
        label = "Parcialmente Verdadeiro",
        description = "Contém fatos reais misturados com dados fora de contexto ou exagero.",
        color = VerdictPartial,
        containerColor = VerdictPartialLight
    ),
    SEM_COMPROVACAO(
        label = "Sem Comprovação / Enganoso",
        description = "Não existem evidências científicas ou jornalísticas que sustentem a alegação.",
        color = VerdictUnverified,
        containerColor = VerdictUnverifiedLight
    );

    companion object {
        fun fromString(value: String): FactCheckVerdict {
            val normalized = value.trim().uppercase()
            return when {
                normalized.contains("VERDADEIRO") && !normalized.contains("PARCIAL") -> VERDADEIRO
                normalized.contains("FALSO") || normalized.contains("FAKE") -> FALSO
                normalized.contains("PARCIAL") -> PARCIALMENTE_VERDADEIRO
                else -> SEM_COMPROVACAO
            }
        }
    }
}

data class ReliableSource(
    val name: String,
    val credibilityType: String, // e.g., "Agência de Checagem", "Órgão Científico / Oficial", "Imprensa Tradicional"
    val citationText: String,
    val url: String = "",
    val isVerifiedSource: Boolean = true
)

data class FactCheckResult(
    val id: String = java.util.UUID.randomUUID().toString(),
    val claimTitle: String,
    val originalText: String,
    val verdict: FactCheckVerdict,
    val verdictExplanation: String,
    val keyPoints: List<String>,
    val sources: List<ReliableSource>,
    val timestamp: Long = System.currentTimeMillis(),
    val category: String = "Geral",
    val isBookmarked: Boolean = false,
    val userPrediction: FactCheckVerdict? = null,
    val tipsToSpotFake: String = ""
)

data class QuizQuestion(
    val id: String,
    val headline: String,
    val context: String,
    val correctVerdict: FactCheckVerdict,
    val detailedExplanation: String,
    val sources: List<ReliableSource>,
    val category: String
)
