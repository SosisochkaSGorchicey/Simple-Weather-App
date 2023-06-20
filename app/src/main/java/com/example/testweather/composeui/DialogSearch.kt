package com.example.testweather.composeui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testweather.ui.theme.Blue
import com.example.testweather.ui.theme.BlueWhite
import com.example.testweather.ui.theme.ErrorColor
import com.example.testweather.ui.theme.NoColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSearch(dialogState: MutableState<Boolean>, onSubmit: (String) -> Unit) {

    val dialogText = rememberSaveable {
        mutableStateOf("")
    }

    AlertDialog(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .wrapContentHeight(),
        containerColor = BlueWhite,
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
            TextButton(onClick = {
                onSubmit(dialogText.value)
                dialogState.value = false
            }) {
                Text(
                    text = "OK",
                    color = Blue,
                    fontSize = 16.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                dialogState.value = false
            }) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Cancel",
                    color = Blue,
                    fontSize = 16.sp
                )
            }
        },
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Enter city name",
                    color = Blue,
                    fontSize = 20.sp
                )
                TextField(
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.None
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    value = dialogText.value,
                    singleLine = true,
                    colors =  TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.White,
                        focusedIndicatorColor = NoColor,
                        unfocusedIndicatorColor = NoColor,
                        disabledIndicatorColor = NoColor,
                        errorIndicatorColor = ErrorColor
                    ),
                    onValueChange = {
                        dialogText.value = it
                    }
                )
            }
        }
    )
}
