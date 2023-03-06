package com.example.timetablemobile.ui.presentation.mainscreen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.timetablemobile.domain.usecase.logout.LogoutUseCase
import com.example.timetablemobile.navigation.Screen
import com.example.timetablemobile.ui.presentation.signinscreen.SignInScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    //will be added later
    //private val getLessonsUseCase: GetLessonsUseCase
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _state: MutableState<MainState> = mutableStateOf(MainState.Initial)
    var state: State<MainState> = _state

    private val _helpDialogIsOpen = mutableStateOf(false)
    var helpDialogIsOpen: State<Boolean> = _helpDialogIsOpen

    init {
        //getLessons()
    }

    private fun getLessons(id: String, startDate: Date, endDate: Date) {
        viewModelScope.launch {

            _state.value = MainState.Loading

            try {
                //will be added later
                //val lessons = getLessonsUseCase()
                //_state.value = MainState.Content(lessons)
            } catch (rethrow: CancellationException) {
                throw rethrow
            } catch (ex: Exception) {
                _state.value = MainState.Error(ex.message ?: "An unexpected error occurred")
            }
        }
    }

    fun logout(
        navController: NavController,
        context: Context
    ) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                logoutUseCase(context = context)
                navController.navigate(Screen.SignInScreen.route) {
                    popUpTo(Screen.MainScreen.route) { inclusive = true }
                }
            } catch (rethrow: CancellationException) {
                throw rethrow
            } catch (ex: Exception) {
                _state.value = MainState.Error(
                    when (ex.message) {
                        "HTTP 401 Unauthorized" -> "Вы не авторизованы"
                        else -> "Что-то пошло не так"
                    }
                )
            }
        }
    }

    fun openDialog() {
        _helpDialogIsOpen.value = true
    }

    fun closeDialog() {
        _helpDialogIsOpen.value = false
    }
}