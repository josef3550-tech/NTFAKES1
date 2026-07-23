package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.FactCheckResult
import com.example.data.model.FactCheckVerdict
import com.example.data.model.ReliableSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity(tableName = "fact_checks")
data class FactCheckEntity(
    @PrimaryKey val id: String,
    val claimTitle: String,
    val originalText: String,
    val verdict: String,
    val verdictExplanation: String,
    val keyPointsJson: String,
    val sourcesJson: String,
    val timestamp: Long,
    val category: String,
    val isBookmarked: Boolean,
    val userPrediction: String?,
    val tipsToSpotFake: String
) {
    fun toFactCheckResult(): FactCheckResult {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
        val stringListAdapter = moshi.adapter<List<String>>(stringListType)

        val sourcesListType = Types.newParameterizedType(List::class.java, ReliableSource::class.java)
        val sourcesAdapter = moshi.adapter<List<ReliableSource>>(sourcesListType)

        val parsedKeyPoints = try {
            stringListAdapter.fromJson(keyPointsJson) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        val parsedSources = try {
            sourcesAdapter.fromJson(sourcesJson) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        return FactCheckResult(
            id = id,
            claimTitle = claimTitle,
            originalText = originalText,
            verdict = FactCheckVerdict.fromString(verdict),
            verdictExplanation = verdictExplanation,
            keyPoints = parsedKeyPoints,
            sources = parsedSources,
            timestamp = timestamp,
            category = category,
            isBookmarked = isBookmarked,
            userPrediction = userPrediction?.let { FactCheckVerdict.fromString(it) },
            tipsToSpotFake = tipsToSpotFake
        )
    }

    companion object {
        fun fromFactCheckResult(result: FactCheckResult): FactCheckEntity {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val stringListType = Types.newParameterizedType(List::class.java, String::class.java)
            val stringListAdapter = moshi.adapter<List<String>>(stringListType)

            val sourcesListType = Types.newParameterizedType(List::class.java, ReliableSource::class.java)
            val sourcesAdapter = moshi.adapter<List<ReliableSource>>(sourcesListType)

            return FactCheckEntity(
                id = result.id,
                claimTitle = result.claimTitle,
                originalText = result.originalText,
                verdict = result.verdict.name,
                verdictExplanation = result.verdictExplanation,
                keyPointsJson = stringListAdapter.toJson(result.keyPoints),
                sourcesJson = sourcesAdapter.toJson(result.sources),
                timestamp = result.timestamp,
                category = result.category,
                isBookmarked = result.isBookmarked,
                userPrediction = result.userPrediction?.name,
                tipsToSpotFake = result.tipsToSpotFake
            )
        }
    }
}
