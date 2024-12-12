package com.gautam.datepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gautam.datepicker.ui.theme.Blue
import com.gautam.datepicker.ui.theme.LightBlue
import com.gautam.datepicker.ui.theme.LightBlue01
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CustomCalender(onDateSelected: (String) -> Unit) {

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    val currentDate = Calendar.getInstance().get(Calendar.DATE)
    val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val currentDay = dateFormat.format(Calendar.getInstance().time)

    val yearRange =
        (currentYear - 50..currentYear + 50).toList() // 50 years before and after the current year

    var showingYear by remember { mutableStateOf(currentYear) }
    var showingDate by remember { mutableStateOf(currentDate) }
    var showingDay by remember { mutableStateOf(currentDay) }
    var showingMonth by remember {
        mutableStateOf(DateFormatSymbols().shortMonths[currentMonth])
    }
    var showingMonthInt by remember {
        mutableStateOf(currentMonth)
    }
    var isYearListVisible by remember { mutableStateOf(false) } // Toggles year list visibility

    // State for calendar dates
    var daysInGrid by remember(showingYear, showingMonthInt) {
        mutableStateOf(getCalendarDates(showingYear, showingMonthInt))
    }

    var selectedDate by remember {
        mutableStateOf(daysInGrid.find { it.day == currentDate && it.isCurrentMonth })
    }

    Surface {
        Card(
            colors = CardDefaults.cardColors(Color.White),
            shape = RoundedCornerShape(corner = CornerSize(25.dp)),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Year Selector
                Box {
                    Surface(
                        shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                        color = Color.White,
                        border = BorderStroke(
                            width = 0.5.dp,
                            color = LightBlue
                        ),
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .clickable {
                                isYearListVisible = !isYearListVisible
                            } // Toggle visibility
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$showingYear",
                                color = Blue,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "DownArrow",
                                tint = LightBlue,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                    // Display Year List if Visible
                    if (isYearListVisible) {
                        DisplayYearList(
                            yearRange = yearRange,
                            currentYear = currentYear,
                            onYearSelected = { selectedYear ->
                                showingYear = selectedYear
                                isYearListVisible = false // Hide the list after selection
                                // Update the calendar when the year is changed
                                daysInGrid = getCalendarDates(showingYear, showingMonthInt)
                            }
                        )
                    }
                }

                // Switching years and month buttons row
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    // Year Back Button
                    IconButton(
                        onClick = {
                            showingYear -= 1
                            daysInGrid = getCalendarDates(showingYear, showingMonthInt)
                        },
                        modifier = Modifier.size(25.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.doubble_back),
                            contentDescription = "Year Back",
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Month Back Button
                    IconButton(
                        onClick = {
                            if (showingMonthInt == 0) {
                                showingMonthInt = 11
                                showingMonth = DateFormatSymbols().shortMonths[showingMonthInt]
                                daysInGrid = getCalendarDates(showingYear, showingMonthInt)
                                showingYear -= 1
                            }else{
                                showingMonthInt -= 1
                                showingMonth = DateFormatSymbols().shortMonths[showingMonthInt]
                                daysInGrid = getCalendarDates(showingYear, showingMonthInt)
                            }

                        },
                        modifier = Modifier.size(25.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowLeft,
                            contentDescription = "Month Back",
                        )
                    }

                    // Current month and year display
                    Text(
                        text = "$showingDate $showingMonth",
                        fontWeight = FontWeight.W800,
                        fontSize = 25.sp,
                        color = Blue,
                    )

                    // Month Forward Button
                    IconButton(
                        onClick = {
                            // Increment the month and adjust the year if needed

                            if (showingMonthInt == 11) {
                                showingMonthInt = 0
                                showingYear += 1
                                showingMonth = DateFormatSymbols().shortMonths[showingMonthInt]
                                daysInGrid = getCalendarDates(showingYear,showingMonthInt)
//                                showingMonthInt += 1
                            }else{
                                showingMonthInt += 1
                                showingMonth = DateFormatSymbols().shortMonths[showingMonthInt]
                                daysInGrid = getCalendarDates(showingYear,showingMonthInt)
                            }
                        },
                        modifier = Modifier.size(25.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowRight,
                            contentDescription = "Month Forward",
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    // Next Year Button
                    IconButton(
                        onClick = {
                            showingYear += 1
                            daysInGrid = getCalendarDates(
                                showingYear,
                                showingMonthInt
                            )
                        },
                        modifier = Modifier.size(25.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.doubble_right),
                            contentDescription = "Next Year",
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                // Current day text
                Text(
                    text = "$showingDay",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(top = 15.dp)
                )

                // Go to today card
                Card(
                    onClick = {
                        // Reset to today's date
                        showingYear = currentYear
                        showingMonth = DateFormatSymbols().shortMonths[currentMonth]
                        showingDate = currentDate
                        daysInGrid = getCalendarDates(currentYear, currentMonth)
                    },
                    shape = RoundedCornerShape(corner = CornerSize(7.dp)),
                    modifier = Modifier.padding(top = 15.dp),
                    colors = CardDefaults.cardColors(LightBlue),
                ) {
                    Text(
                        text = "GO TO TODAY",
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }

                // Display the names of the days of the week
                val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    daysOfWeek.forEach { day ->
                        Text(
                            text = day,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Blue,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 5.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Dates grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(daysInGrid) { dateInfo ->
                        DateCell(
                            dateInfo = dateInfo,
                            isSelected = dateInfo == selectedDate, // Compare current cell with selectedDate
                            onClick = {
                                selectedDate = dateInfo
                                onDateSelected("${dateInfo.day} $showingMonth $showingYear")
                            } // Update selected date
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun DateCell(
    dateInfo: DateInfo,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val boxBg = if (isSelected) LightBlue else Color.White
    val textColor =
        if (isSelected) Color.White else if (dateInfo.isCurrentMonth) Color.Black else Color.LightGray

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clickable { onClick() }, // Invoke the passed onClick handler
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(boxBg)
        ) {
            Text(
                text = dateInfo.day.toString(),
                fontSize = 16.sp,
                fontWeight = if (dateInfo.isCurrentMonth) FontWeight.Bold else FontWeight.Normal,
                color = textColor,
                modifier = Modifier
                    .padding(10.dp)
                    .size(40.dp),
                maxLines = 1
            )
        }
    }
}


@Composable
fun DisplayYearList(
    yearRange: List<Int>,
    currentYear: Int,
    onYearSelected: (Int) -> Unit,
) {
    val lazyListState = rememberLazyListState() // LazyColumn scroll state
    val coroutineScope = rememberCoroutineScope()

    // Scroll to the current year when the list is first displayed
    LaunchedEffect(Unit) {
        val currentYearIndex = yearRange.indexOf(currentYear)
        if (currentYearIndex != -1) {
            coroutineScope.launch {
                lazyListState.scrollToItem(currentYearIndex)
            }
        }
    }

    Card(
        colors = CardDefaults.cardColors(LightBlue01),
        shape = RoundedCornerShape(CornerSize(10.dp)),
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(200.dp) // Restrict height for better scrolling
    ) {
        LazyColumn(state = lazyListState) {
            itemsIndexed(yearRange) { _, year ->
                Text(
                    text = "$year",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier
                        .clickable { onYearSelected(year) }
                        .padding(
                            horizontal = 15.dp,
                            vertical = 5.dp
                        ),
                    color = Blue
                )
            }
        }
    }
}


data class DateInfo(
    val day: Int,
    val isCurrentMonth: Boolean,
)


fun getCalendarDates(year: Int, month: Int): List<DateInfo> {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)

    // Get the first day of the week for the current month (Sunday = 1, Saturday = 7)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    // Calculate how many previous month's dates we need to display
    val daysFromPreviousMonth = if (firstDayOfWeek == Calendar.SUNDAY) 0 else firstDayOfWeek - Calendar.SUNDAY

    // Move back to the previous month if necessary
    val previousMonth = if (month == 0) 11 else month - 1
    val previousYear = if (month == 0) year - 1 else year

    // Get the last day of the previous month
    val lastDayOfPreviousMonth = Calendar.getInstance().apply {
        set(previousYear, previousMonth, 1)
        add(Calendar.MONTH, 1)
        add(Calendar.DAY_OF_MONTH, -1)
    }

    // Adjust the calendar to the start of the previous month to fill the previous month's dates
    calendar.add(Calendar.DAY_OF_MONTH, -daysFromPreviousMonth)

    val dates = mutableListOf<DateInfo>()

    // Add the previous month's days to fill the start of the grid
    for (i in 0 until 42) { // 6 rows × 7 columns = 42 cells in total
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val isCurrentMonth = calendar.get(Calendar.MONTH) == month
        dates.add(
            DateInfo(
                day = day,
                isCurrentMonth = isCurrentMonth
            )
        )
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return dates
}


@Preview
@Composable
private fun CustomCalenderPrev() {
    CustomCalender({})
}
