package ph.edu.comteq.pinililab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.pinililab3.ui.theme.PiniliLab3Theme
import java.time.Duration
import java.time.Instant

class TicketingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PiniliLab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Ticketing(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ticketing(modifier: Modifier = Modifier) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now()
            .plus(Duration.ofDays(2)).toEpochMilli(),
        selectableDates = object: SelectableDates{
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= Instant.now()
                    .plus(Duration.ofDays(1)).toEpochMilli()
            }
        }
    )
   Column(
       modifier = modifier.background(Color.Black)
   ){
        Column(
            modifier = Modifier.weight(1f)
                .verticalScroll(rememberScrollState())
        ){
            //header
            Box(
                modifier = Modifier.fillMaxWidth().height(230.dp),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.mesuem),
                    contentDescription = "Museum",
                    modifier = Modifier.fillMaxWidth().height(230.dp),
                    contentScale = ContentScale.Crop
                )
                //Black overlay
                Box(
                    modifier = Modifier.fillMaxWidth().height(230.dp)
                        .background(Color.Black.copy(alpha = 0.6f))
                )
                Text(
                    "Official\nTicketing Service",
                    fontSize = 32.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }
            //Inner Container for date and ticket types
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ){
                DatePicker(
                    modifier = Modifier.padding(0.dp).fillMaxWidth(),
                    state = datePickerState,
                    title = null,
                    headline = {
                        Text(
                            "1. Date to Visit",
                            fontSize = 26.sp,
                            fontFamily = playfairdisplayregular

                        )
                    },
                    colors = DatePickerDefaults.colors(
                        titleContentColor = Color(0xFFF8B902),
                        headlineContentColor = Color(0xFFF8B902),
                        weekdayContentColor = Color(0xFFF8B902),
                        containerColor = Color.Transparent,
                        dayContentColor = Color.White,
                        todayContentColor = Color(0xFFF8B902),
                        todayDateBorderColor = Color(0xFFF8B902),
                        selectedDayContainerColor = Color(0xFFF8B902),
                        selectedDayContentColor = Color.Black,
                        disabledDayContentColor = Color.Gray

                    )
                )
                //general admission ticket

                //free tickets
            }
        }
       //Button bar for Totals
       Row(
           modifier = Modifier.fillMaxWidth().height(80.dp)
               .background(Color(0xFFF8B902))
               .padding(horizontal = 20.dp),
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically
       ){
           Text(
               "Total: P500",
               fontSize = 26.sp,
               fontFamily = playfairdisplayregular,
               color = Color.Black
           )
           Button(
               modifier = Modifier.padding(5.dp),
               onClick = { /*todo*/},
               colors = ButtonDefaults.buttonColors(
                   containerColor = Color.Black,
               )
           ) {
               Text(
                   "Checkout",
                   fontSize = 20.sp,
                   fontFamily = playfairdisplayregular,
                   color = Color(0xFFF8B902)
               )
           }
       }
   }
}



@Preview(showBackground = true)
@Composable
fun TicketingPreview() {
    PiniliLab3Theme {
        Ticketing(modifier = Modifier)
    }
}