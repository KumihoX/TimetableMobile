package com.example.timetablemobile.ui.presentation.signinscreen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.timetablemobile.data.remote.dto.LoginDto
import com.example.timetablemobile.data.remote.dto.UserInfoDto
import com.example.timetablemobile.domain.usecase.login.LoginUseCase
import com.example.timetablemobile.domain.usecase.token.SaveTokenUseCase
import com.example.timetablemobile.domain.usecase.userInfo.InfoUseCase
import com.example.timetablemobile.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,
    private val infoUseCase: InfoUseCase
) : ViewModel() {
    private val _state: MutableState<SignInScreenState> = mutableStateOf(SignInScreenState.Initial)
    var state: State<SignInScreenState> = _state

    private val _login = mutableStateOf("")
    var login: State<String> = _login

    private val _password = mutableStateOf("")
    var password: State<String> = _password

    private val _fieldsState = mutableStateOf(false)
    var fieldsState: State<Boolean> = _fieldsState

    private var scheduleType = ""
    private var typeData = ""


    private fun checkingFields() {
        _fieldsState.value = !(login.value.isNullOrEmpty()
                || password.value.isNullOrEmpty())
    }

    private fun defineUser(userInfo: UserInfoDto) {
        if (!userInfo.teacherId.isNullOrEmpty()) {
            scheduleType = "TEACHER"
            typeData = userInfo.teacherId
        }
        if (userInfo.group != null) {
            scheduleType = "STUDENT"
            typeData = userInfo.group.toString()
        }
    }

    fun login(
        navController: NavController,
        context: Context
    ) {

        val userData = LoginDto(
            login = _login.value,
            password = _password.value
        )

        viewModelScope.launch {
            _state.value = SignInScreenState.Loading

            try {
                val token = loginUseCase(userData)
                _state.value = SignInScreenState.Content(token)

                val saveTokenUseCase = SaveTokenUseCase(context)
                saveTokenUseCase.execute(token)

                val userData = infoUseCase(context = context)
                defineUser(userData)

                navController.navigate(
                    Screen.MainScreen.passScheduleInfo(
                        type = scheduleType,
                        data = typeData
                    )) {
                    popUpTo(Screen.SignInScreen.route) { inclusive = true }
                }
            } catch (rethrow: CancellationException) {
                throw rethrow
            } catch (ex: Exception) {
                _state.value = SignInScreenState.Error(
                    when (ex.message) {
                        "HTTP 400 Bad Request" -> "Введенные данные неверны"
                        else -> "Что-то пошло не так"
                    }
                )
            }
        }
    }


    fun onLoginChange(newLogin: String) {
        _login.value = newLogin
        checkingFields()
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        checkingFields()
    }
}
