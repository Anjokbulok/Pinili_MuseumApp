package ph.edu.comteq.pinililab3

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.pinililab3.ui.theme.PiniliLab3Theme
import kotlin.collections.forEach

class ArtistPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PiniliLab3Theme {
                AppScreen()
            }
        }
    }
}

data class Artwork(
    val title: String,
    val years: String,
    val born_at: String,
    val comment: String
)

data class Artist(
    val name: String,
    val years: String,
    val avatarResId: Int,
    val artworkNames: List<String>,
    val artworkImageResIds: List<Int>
)

data class ExhibitData(
    val artwork: Artwork,
    val imageResId: Int
)

object MockData {
    // NOTE: You must have these drawable resources in your res/drawable folder
    val leonardoDaVinci = Artist(
        name = "Leonardo da Vinci",
        years = "1452 - 1519",
        avatarResId = R.drawable.leonardo_da_vinci,
        artworkNames = listOf("Mona Lisa", "Lady Ermine", "Litta Madonna"),
        artworkImageResIds = listOf(
            R.drawable.mona_lisa,
            R.drawable.lady_ermine,
            R.drawable.litta_madonna
        )
    )

    val michelangelo = Artist(
        name = "Michelangelo",
        years = "1475 - 1564",
        avatarResId = R.drawable.michelangelo,
        artworkNames = listOf("David", "Delphic Sibyl", "Torment of Saint Anthony"),
        artworkImageResIds = listOf(
            R.drawable.david,
            R.drawable.delphic_sibyl,
            R.drawable.torment_of_saint_anthony
        )
    )

    val gustavKlimt = Artist(
        name = "Gustav Klimt",
        years = "1862 - 1918",
        avatarResId = R.drawable.gustav_klimt,
        artworkNames = listOf("Adele Bloch-Bauer", "Lady with Fan", "The Kiss"),
        artworkImageResIds = listOf(
            R.drawable.adele_bloch_bauer,
            R.drawable.lady_with_fan,
            R.drawable.the_kiss
        )
    )


    val artists = listOf(leonardoDaVinci, michelangelo, gustavKlimt)
}

fun loadArtworksFromJson(context: Context): List<Artwork> {
    return try {
        val fileName = "artworks.json"

        val inputStream = context.assets.open(fileName)
        val jsonString = inputStream.bufferedReader().use { it.readText() }


        val gson = com.google.gson.Gson()
        val artworkArray = gson.fromJson(jsonString, Array<Artwork>::class.java)

        Log.d("JSON_DEBUG", "Loaded ${artworkArray.size} artworks successfully using GSON")
        artworkArray.toList()
    } catch (e: Exception) {
        Log.e("JSON_ERROR", "Failed to load artworks.json from assets (GSON): ${e.message}", e)

        listOf(
            Artwork(
                title = "Error Loading JSON",
                years = "N/A",
                born_at = "N/A",
                comment = "Failed to load artworks.json. Check 1) JSON file syntax and 2) GSON dependency. Default data is shown for one item."
            )
        )
    }
}


// --- 3. UI Colors/Constants ---

val RenaissanceGold = Color(0xFFC7A747)
val BackgroundColor = Color(0xFFEDEADF)
val DarkBackground = Color(0xFF262626)
val ArtworkCornerRadius = 16.dp
val ImageArcRadius = 80.dp



@Composable
fun AppScreen() {
    val context = LocalContext.current

    val allArtworks = remember { loadArtworksFromJson(context) }

    // State to hold the selected artwork's (Name, ImageResId) for navigation
    var selectedArtworkData by remember { mutableStateOf<Pair<String, Int>?>(null) }

    val exhibitData = remember(selectedArtworkData) {
        val (name, imageId) = selectedArtworkData ?: Pair("Lady Ermine", R.drawable.lady_ermine)

        val nameToMatch = name.trim().lowercase()

        val artworkDetails = allArtworks.firstOrNull { it.title.trim().lowercase() == nameToMatch }

        val isErrorState = allArtworks.size == 1 && allArtworks.first().title.contains("Error")

        val finalArtwork = if (isErrorState) {
            allArtworks.first()
        } else {
            // Fallback to the specific image shown in the example if details aren't found
            artworkDetails
                ?: allArtworks.firstOrNull { it.title.trim().lowercase() == "lady ermine" }
                ?: Artwork(
                    title = name,
                    years = "Data Missing",
                    born_at = "Unknown",
                    comment = "Could not find specific data for '$name' in JSON."
                )
        }

        ExhibitData(
            artwork = finalArtwork,
            imageResId = imageId
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        // Background texture is applied to the Surface here
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background Texture",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize().alpha(0.2f) // Make the texture subtle
        )

        // Navigation switch: Show Exhibit Page if an artwork is selected, otherwise show Artist Page
        if (selectedArtworkData != null) {
            ExhibitPage(
                exhibitData = exhibitData,
                onBackClick = { selectedArtworkData = null }
            )
        } else {
            ArtistPageContent(
                artists = MockData.artists,
                onArtworkClick = { name, resId -> selectedArtworkData = Pair(name, resId) }
            )
        }
    }
}



@Composable
fun ArtistPageContent(artists: List<Artist>, onArtworkClick: (String, Int) -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Artists", "Artworks")

    Column(
        modifier = Modifier
            .fillMaxSize()
            // ⭐️ ADJUSTMENT 1: Apply system bars padding to the top-level scrollable content
            .padding(WindowInsets.systemBars.asPaddingValues())
            .verticalScroll(rememberScrollState())
    ) {

        HeaderSection(title = "Explore the art of\nRenaissance")

        TabSection(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (selectedTabIndex == 0) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                artists.forEach { artist ->
                    ArtistItem(artist = artist, onArtworkClick = onArtworkClick)
                    // Divider matching the design aesthetic
                    Divider(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        } else {
//            Text(
//                text = "Artworks content coming soon...",
//                modifier = Modifier.padding(20.dp),
//                color = Color.Black
//            )
        }
    }
}

@Composable
fun ArtistItem(artist: Artist, onArtworkClick: (String, Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Image(
                painter = painterResource(id = artist.avatarResId),
                contentDescription = artist.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = artist.name,
                    fontWeight = FontWeight.SemiBold, // Use SemiBold for consistency
                    fontSize = 20.sp, // Slightly larger font
                    color = Color.Black
                )
                Text(
                    text = artist.years,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Increased spacing
        ) {
            items(artist.artworkImageResIds.size) { index ->
                val artworkName = artist.artworkNames.getOrElse(index) { "Unknown Artwork" }
                val imageResId = artist.artworkImageResIds[index]

                ArtworkImage(
                    resourceId = imageResId,
                    onClick = { onArtworkClick(artworkName, imageResId) }
                )
            }
        }
    }
}

@Composable
fun ArtworkImage(resourceId: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp)) // 👈 square image with slight rounding
            .clickable(onClick = onClick)
    )
}


@Composable
fun HeaderSection(title: String) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = title.substringBefore('\n'),
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black
        )
        Text(
            text = title.substringAfter('\n'),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold, // Bold for the highlight
            color = RenaissanceGold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Type to search...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search icon", tint = Color.Gray) },
            trailingIcon = {
                Row {
                    Icon(
                        Icons.Filled.Fullscreen,
                        contentDescription = "Fullscreen icon",
                        tint = Color.Gray
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RenaissanceGold,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = RenaissanceGold,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

@Composable
fun TabSection(tabs: List<String>, selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        tabs.forEachIndexed { index, title ->
            Column(
                modifier = Modifier
                    .padding(end = 40.dp)
                    .clickable { onTabSelected(index) },
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    color = if (selectedTabIndex == index) Color.Black else Color.Gray,
                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (selectedTabIndex == index) {
                    Divider(
                        color = RenaissanceGold,
                        thickness = 3.dp,
                        modifier = Modifier.width(60.dp)
                    )
                }
            }
        }
    }
}

// --- 6. Exhibit Page Composable (Fixed for visual alignment and functionality) ---

@Composable
fun ExhibitPage(exhibitData: ExhibitData, onBackClick: () -> Unit) {
    val artwork = exhibitData.artwork
    val imageResId = exhibitData.imageResId

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // ⭐️ ADJUSTMENT 2: Apply system bars padding to the top-level scrollable content
                .padding(WindowInsets.systemBars.asPaddingValues())
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Spacer removed, as systemBars padding now handles the top offset

            // 1. Artwork Image and Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(450.dp)
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = artwork.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        // FIX: Use top-only rounding for the arch effect
                        .clip(RoundedCornerShape(topStart = ImageArcRadius, topEnd = ImageArcRadius))
                )

                // Gold Text/Arrow Container
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(RenaissanceGold),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(start = 20.dp)
                    ) {
                        Text(
                            text = artwork.title,
                            color = DarkBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${artwork.years}, ${artwork.born_at}",
                            color = DarkBackground.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                    // Arrow icon/Back Click Button
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(DarkBackground)
                            // FIX: Make the arrow click navigate BACK
                            .clickable(onClick = onBackClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Go back to list",
                            tint = RenaissanceGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // 2. Comment Area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 40.dp, bottom = 40.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Quote image (from drawable)
                Image(
                    painter = painterResource(id = R.drawable.quote),
                    contentDescription = "Quote icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(70.dp)              // slightly larger for better proportion
                        .alpha(0.25f)             // softer transparency
                        .offset(x = (-5).dp, y = (-10).dp)
                        .padding(top = 6.dp)
                )

                // Comment text
                Text(
                    text = artwork.comment,
                    color = Color.White,
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, top = 8.dp) // balanced spacing from image
                )
            }

        }
    }
}
