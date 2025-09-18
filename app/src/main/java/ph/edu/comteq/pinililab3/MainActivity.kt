package ph.edu.comteq.pinililab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.pinililab3.ui.theme.PiniliLab3Theme

val playfairdisplayregular = FontFamily(
    Font(R.font.playfairdisplayregular, FontWeight.Normal)
)

val optima = FontFamily(
    Font(R.font.optima, FontWeight.Normal)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PiniliLab3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Homepage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Homepage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .background(Color.DarkGray)
    )
    {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(40.dp)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(25.dp))

            Surface (
                shape = RoundedCornerShape(10.dp),
                shadowElevation = 10.dp,
                color = Color.LightGray

            ) {
                Image(
                    painter = painterResource(id = R.drawable.louvre),
                    contentDescription = "Louvre",
                    modifier = Modifier
                        .size(450.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Experience Art",
                    fontSize = 40.sp,
                    fontFamily = playfairdisplayregular,
                    textAlign = TextAlign.Center,
                    color = Color.Yellow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
            Text(
                text = "We are thrilled to invite you to join us for " +
                        "an extraordinary event that will immerse you in the world of art",
                fontSize = 16.sp,
                fontFamily = optima,

                color = Color.White,
                textAlign = TextAlign.Center,   // Center all lines
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            Button(
                onClick = {
                    val intent = Intent(context, ExploreActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow
                ),
                shape = RoundedCornerShape(4.dp)
            )
            {
                Text(
                    text = "Explore Now",
                    fontFamily = playfairdisplayregular,
                    fontSize = 28.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    PiniliLab3Theme {
        Homepage()
    }
}