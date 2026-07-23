package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.FactCheckResult
import com.example.data.model.FactCheckVerdict
import com.example.data.model.QuizQuestion
import com.example.data.repository.FactCheckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FactCheckViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FactCheckRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = FactCheckRepository(database.factCheckDao())
    }

    // Saved checks from Room
    val historyList: StateFlow<List<FactCheckResult>> = repository.savedFactChecks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bookmarkedList: StateFlow<List<FactCheckResult>> = repository.bookmarkedFactChecks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Form Inputs
    private val _claimInput = MutableStateFlow("")
    val claimInput: StateFlow<String> = _claimInput.asStateFlow()

    private val _userPrediction = MutableStateFlow<FactCheckVerdict?>(null)
    val userPrediction: StateFlow<FactCheckVerdict?> = _userPrediction.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Geral")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Analysis Loading State
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    // Active Result View
    private val _activeResult = MutableStateFlow<FactCheckResult?>(null)
    val activeResult: StateFlow<FactCheckResult?> = _activeResult.asStateFlow()

    // Trending Items
    val trendingList: List<FactCheckResult> = repository.getTrendingFactChecks()

    // Quiz State
    val quizList: List<QuizQuestion> = repository.getQuizQuestions()
    
    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _quizSelectedVerdict = MutableStateFlow<FactCheckVerdict?>(null)
    val quizSelectedVerdict: StateFlow<FactCheckVerdict?> = _quizSelectedVerdict.asStateFlow()

    private val _quizAnswerSubmitted = MutableStateFlow(false)
    val quizAnswerSubmitted: StateFlow<Boolean> = _quizAnswerSubmitted.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    // UI Actions
    fun updateClaimInput(text: String) {
        _claimInput.value = text
    }

    fun selectUserPrediction(verdict: FactCheckVerdict) {
        _userPrediction.value = if (_userPrediction.value == verdict) null else verdict
    }

    fun updateCategory(category: String) {
        _selectedCategory.value = category
    }

    fun verifyClaim(onSuccess: () -> Unit) {
        val text = _claimInput.value.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            _isAnalyzing.value = true
            try {
                val result = repository.verifyClaimWithAI(
                    text = text,
                    userPrediction = _userPrediction.value,
                    category = _selectedCategory.value
                )
                _activeResult.value = result
                _isAnalyzing.value = false
                onSuccess()
            } catch (e: Exception) {
                _isAnalyzing.value = false
            }
        }
    }

    fun selectResultToView(result: FactCheckResult) {
        _activeResult.value = result
    }

    fun toggleBookmark(result: FactCheckResult) {
        viewModelScope.launch {
            repository.toggleBookmark(result.id, result.isBookmarked)
            // Update active result if it's the current one
            if (_activeResult.value?.id == result.id) {
                _activeResult.value = _activeResult.value?.copy(isBookmarked = !result.isBookmarked)
            }
        }
    }

    // Quiz Actions
    fun selectQuizVerdict(verdict: FactCheckVerdict) {
        if (!_quizAnswerSubmitted.value) {
            _quizSelectedVerdict.value = verdict
        }
    }

    fun submitQuizAnswer() {
        val selected = _quizSelectedVerdict.value ?: return
        val currentQuestion = quizList.getOrNull(_currentQuizIndex.value) ?: return

        _quizAnswerSubmitted.value = true
        if (selected == currentQuestion.correctVerdict) {
            _quizScore.value += 1
        }
    }

    fun nextQuizQuestion() {
        if (_currentQuizIndex.value < quizList.size - 1) {
            _currentQuizIndex.value += 1
            _quizSelectedVerdict.value = null
            _quizAnswerSubmitted.value = false
        }
    }

    fun resetQuiz() {
        _currentQuizIndex.value = 0
        _quizSelectedVerdict.value = null
        _quizAnswerSubmitted.value = false
        _quizScore.value = 0
    }
}
