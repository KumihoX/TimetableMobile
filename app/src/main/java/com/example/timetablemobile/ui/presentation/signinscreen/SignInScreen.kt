package com.example.timetablemobile.ui.presentation.signinscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.timetablemobile.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.timetablemobile.navigation.Screen
import com.example.timetablemobile.ui.theme.MainGreen
import com.example.timetablemobile.ui.theme.Red

@Composable
fun SignInScreen (
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Logo()
            LoginField(viewModel = viewModel)
            PasswordField(viewModel = viewModel)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            LogIn(viewModel = viewModel)
            WithoutAuth(navController = navController)
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun Logo() {
    Image(
        painter = painterResource(R.drawable.cat_love),
        contentDescription = "Лого",
        modifier = Modifier.padding(0.dp, 64.dp, 0.dp, 32.dp)
    )
}

@Composable
fun LoginField(viewModel: SignInViewModel) {
    val login: String by remember { viewModel.login }
    val correct: Boolean by remember { viewModel.correct }

    OutlinedTextField(
        value = login,
        onValueChange = {viewModel.onLoginChange(it)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 4.dp),
        placeholder = {
            Text(
                stringResource(R.string.login),
                style = MaterialTheme.typography.body2,
                color = MainGreen
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        enabled = true,
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MainGreen,
            placeholderColor = MainGreen,
            cursorColor = MainGreen,
            textColor = MainGreen,
            focusedBorderColor = MainGreen
        )
    )
    if (!correct) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 16.dp),
            text = stringResource(R.string.invalidEmailMessage),
            color = Red,
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Start
        )
    }
    else {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 12.dp))
    }
}

@Composable
fun PasswordField(viewModel: SignInViewModel) {
    val password: String by remember { viewModel.password }

    OutlinedTextField(
        value = password,
        onValueChange = {viewModel.onPasswordChange(it)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 16.dp),
        placeholder = {
            Text(
                stringResource(R.string.password),
                style = MaterialTheme.typography.body2,
                color = MainGreen
            )
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        enabled = true,
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MainGreen,
            placeholderColor = MainGreen,
            cursorColor = MainGreen,
            textColor = MainGreen,
            focusedBorderColor = MainGreen
        )
    )
}

@Composable
fun LogIn(viewModel: SignInViewModel) {
    val buttonState: Boolean by remember { viewModel.fieldsState }
    Button(
        onClick = { viewModel.login() },
        enabled = buttonState,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MainGreen,
            contentColor = White,
            disabledBackgroundColor = White,
            disabledContentColor = MainGreen
        ),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, MainGreen),
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
    ) {
        Text(
            stringResource(R.string.sign_in),
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            fontSize = 16.sp
        )
    }
}

@Composable
fun WithoutAuth(navController: NavController) {
    TextButton(
        onClick = {
                  navController.navigate(Screen.UnsignedScreen.route)
                  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp, 0.dp, 16.dp)

    ) {
        Text(
            stringResource(R.string.without_auth),
            color = MainGreen,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            fontSize = 16.sp
        )
    }
}