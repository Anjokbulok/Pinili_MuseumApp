package ph.edu.comteq.pinililab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.delay
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

    // Museum animation
    val museumOffsetY = remember { Animatable(-300f) }
    val museumAlpha = remember { Animatable(0f) }

    // Typing texts
    val fullTitle = "Experience Art"
    var typedTitle by remember { mutableStateOf("") }

    val fullIntro =
        "We are thrilled to invite you to join us for an extraordinary event that will immerse you in the world of art"
    var typedIntro by remember { mutableStateOf("") }

    // NEW: Button visibility state + animation
    var showButton by remember { mutableStateOf(false) }
    val buttonAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Museum animation
        museumOffsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(1200, easing = FastOutSlowInEasing)
        )
        museumAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(800)
        )

        // Title typing
        typedTitle = ""
        fullTitle.forEachIndexed { index, _ ->
            typedTitle = fullTitle.substring(0, index + 1)
            delay(40)
        }

        // Intro typing
        typedIntro = ""
        fullIntro.forEachIndexed { index, _ ->
            typedIntro = fullIntro.substring(0, index + 1)
            delay(15)
        }

        // NEW: Show button only after intro typing finishes
        showButton = true

        // Button fade-in animation
        buttonAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(800, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .background(Color.DarkGray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(40.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Museum card animation
            Surface(
                shape = RoundedCornerShape(10.dp),
                shadowElevation = 10.dp,
                color = Color.LightGray,
                modifier = Modifier.graphicsLayer {
                    alpha = museumAlpha.value
                    translationY = museumOffsetY.value
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.louvre),
                    contentDescription = "Louvre",
                    modifier = Modifier.size(350.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title typing
            Text(
                text = typedTitle,
                fontSize = 40.sp,
                fontFamily = playfairdisplayregular,
                textAlign = TextAlign.Center,
                color = Color.Yellow,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Intro typing
            Text(
                text = typedIntro,
                fontSize = 16.sp,
                fontFamily = optima,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            // BUTTON APPEARS LAST
            if (showButton) {
                Button(
                    onClick = {
                        val intent = Intent(context, ExploreActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = buttonAlpha.value
                        }
                        .padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
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
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    PiniliLab3Theme {
        Homepage()
    }
}
