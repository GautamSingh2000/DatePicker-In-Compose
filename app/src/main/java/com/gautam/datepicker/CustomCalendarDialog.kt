import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gautam.datepicker.CustomCalender

@Composable
fun CustomCalendarDialog(
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    // Overlay background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(16.dp))// Semi-transparent background
    ) {
        // Dialog box
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Title
                Text(
                    text = "Select a Date",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Calendar
                val selectedDate = remember { mutableStateOf("") }
                CustomCalender(
                    onDateSelected = { date ->
                        selectedDate.value = date
                    }
                )
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Button(onClick = { onDateSelected(selectedDate.value) }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun dialogPreview() {
    CustomCalendarDialog({},{})
}
