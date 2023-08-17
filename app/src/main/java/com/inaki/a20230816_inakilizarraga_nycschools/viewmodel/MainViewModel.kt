package com.inaki.a20230816_inakilizarraga_nycschools.viewmodel

import com.inaki.a20230816_inakilizarraga_nycschools.model.schools.School
import com.inaki.a20230816_inakilizarraga_nycschools.model.scores.Score
import com.inaki.a20230816_inakilizarraga_nycschools.usecases.UseCasesWrapper
import com.inaki.a20230816_inakilizarraga_nycschools.utils.BaseViewModel
import com.inaki.a20230816_inakilizarraga_nycschools.utils.NoSchoolDBNException
import com.inaki.a20230816_inakilizarraga_nycschools.utils.NoSchoolSelectedException
import com.inaki.a20230816_inakilizarraga_nycschools.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: UseCasesWrapper
) : BaseViewModel() {

    var selectedSchool: School? = null

    var scoresCache: List<Score>? = null
    private lateinit var schoolsCache: List<School>

    private val _schools: MutableStateFlow<State<List<School>>> = MutableStateFlow(State.LOADING)
    val schools: StateFlow<State<List<School>>> get() = _schools

    private val _score: MutableStateFlow<State<List<Score>>> = MutableStateFlow(State.LOADING)
    val score: StateFlow<State<List<Score>>> get() = _score

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    init {
        getSchools()
        getScores()
    }

    fun schoolScore() {
        selectedSchool?.let {sc ->
            sc.dbn?.let { dbn ->
                val newScores = scoresCache?.filter { it.dbn == dbn }
                _score.value = State.SUCCESS(newScores ?: emptyList())
            } ?: let { _score.value = State.ERROR(NoSchoolDBNException()) }
        } ?: let { _score.value = State.ERROR(NoSchoolSelectedException()) }
    }

    fun onOpenErrorDialog() {
        _showDialog.value = true
    }

    fun onDialogRetry(retrySchools: Boolean = true) {
        _showDialog.value = false
        if (retrySchools) getSchools() else getScores()
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    private fun getScores() {
        safeViewModelScope.launch {
            useCases.scoreUseCase().collect { state ->
                when (state) {
                    is State.SUCCESS -> scoresCache = state.result
                    else -> _score.value = state
                }
            }
        }
    }

    private fun getSchools() {
        safeViewModelScope.launch {
            useCases.schoolUseCase().collect {
                if (it is State.SUCCESS) {
                    schoolsCache = it.result
                }
                _schools.value = it
            }
        }
    }

    fun performQuery(onQueryChanged: String, noQuery: Boolean) {
        val newSchools = schoolsCache.filter { it.zip == onQueryChanged }
        _schools.value = State.SUCCESS(if (noQuery) schoolsCache else newSchools)
    }
}