package com.gautam.datepicker

import CustomCalendarDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gautam.datepicker.ui.theme.DatePickerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatePickerTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(this@MainActivity)
            }
        }
    }
}


@Composable
fun MainScreen(context : Context) {
    val showDialog = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { showDialog.value = true }) {
            Text("Get Calendar")
        }

        if (showDialog.value) {
            CustomCalendarDialog(
                onDateSelected = { date ->
                    selectedDate.value = date
                    coroutineScope.launch {
                        Toast.makeText(context, "You chose: ${selectedDate.value}", Toast.LENGTH_LONG).show()
                    }
                    showDialog.value = false // Close dialog
                },
                onDismissRequest = {
                    coroutineScope.launch {
                        Toast.makeText(context, "Did not chose any date !!", Toast.LENGTH_LONG).show()
                    }
                    showDialog.value = false // Close dialog
                }
            )
        }
    }
}

