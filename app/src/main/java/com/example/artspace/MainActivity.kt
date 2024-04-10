package com.example.artspace

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.artspace.ui.theme.ArtSpaceTheme

data class ArtPiece(@DrawableRes val imageResId: Int, @StringRes val titleResId: Int, @RawRes val soundResId: Int )

class MainActivity : ComponentActivity() {

    private lateinit var backgroundMediaPlayer: MediaPlayer

    private var mediaPlayer: MediaPlayer? = null

    private fun playArtPieceSound(@RawRes soundResId: Int) {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = MediaPlayer.create(this, soundResId).apply {
            start() // Play sound effect or narration for the current art piece
            setOnCompletionListener {
                it.release()
                mediaPlayer = null // Reset the mediaPlayer instance
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {

                    val artPieces = listOf(
                        ArtPiece(R.drawable.pic1, R.string.pic1_string, R.raw.pic1_sound),
                        ArtPiece(R.drawable.pic2, R.string.pic2_string, R.raw.pic2_sound),
                        ArtPiece(R.drawable.pic3, R.string.pic3_string, R.raw.pic3_sound),
                        ArtPiece(R.drawable.pic4, R.string.pic4_string, R.raw.pic4_sound),
                        ArtPiece(R.drawable.pic5, R.string.pic5_string, R.raw.pic5_sound)
                    )
                    // Remember the current index
                    var currentIndex by remember { mutableStateOf(0) }

                    Column (modifier = Modifier
                        .verticalScroll(rememberScrollState())) {
                        ShowPicture(
                            imageResId = artPieces[currentIndex].imageResId,
                            contentDescriptionResId = artPieces[currentIndex].titleResId
                        )

                        ShowTitle(artPieces[currentIndex].titleResId)

                        ShowButtons(
                            onPrevious = {
                                currentIndex = (currentIndex - 1 + artPieces.size) % artPieces.size
                                playArtPieceSound(artPieces[currentIndex].soundResId)
                            },
                            onNext = {
                                currentIndex = (currentIndex + 1) % artPieces.size
                                //this keep curshing investigate why
                                playArtPieceSound(artPieces[currentIndex].soundResId)
                            }
                        )
                    }
                }
            }
        }

        backgroundMediaPlayer = MediaPlayer.create(this, R.raw.bckgmusic).apply {
            isLooping = true // Set the MediaPlayer to loop the background music
            start() // Start playing the background music
       }
}

//
//@Composable
//fun ShowPicture(
//    @DrawableRes imageResId: Int,
//    @StringRes contentDescriptionResId: Int,
//    modifier: Modifier = Modifier
//) {
//    val contentDescription = stringResource(id = contentDescriptionResId)
//    Image(
//        painter = painterResource(id = imageResId),
//        contentDescription = contentDescription,
//        modifier = modifier.
//        size(400.dp)
//            .padding(16.dp),
//        contentScale = ContentScale.Crop
//    )
//}
@Composable
fun ShowPicture(
    @DrawableRes imageResId: Int,
    @StringRes contentDescriptionResId: Int,
    modifier: Modifier = Modifier
) {
    val contentDescription = stringResource(id = contentDescriptionResId)
    Crossfade(
        targetState = imageResId,
        modifier = modifier.fillMaxSize(),
        animationSpec = tween(durationMillis = 2000) // Adjust durationMillis as needed
    ) { targetImage ->
        Image(
            painter = painterResource(id = targetImage),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentScale = ContentScale.Crop
        )
    }
}



@Composable
fun ShowTitle(@StringRes artText: Int,
    modifier: Modifier = Modifier
){
    val artData = stringResource(id = artText)
    Column(){
        Text(artData,
            color = Color.White,
            textAlign = TextAlign.Center
            )

    }
}

@Composable
fun ShowButtons(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
    ) {
    val context = LocalContext.current
    val darkBlue = colorResource(id = R.color.dark_blue)
    fun playPageTurnSound() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.pageturn)
        mediaPlayer?.start() // Start playback

        // Release the MediaPlayer once the sound has finished playing
        mediaPlayer?.setOnCompletionListener { mp ->
            mp.release()
        }
    }

    Row() {
        Button(onClick = {
            playPageTurnSound()
            onPrevious() },
            modifier = modifier,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = darkBlue // Use the loaded color for the button's background
//            )
        ) {
            Text("Previous (הקודם)")

        }
        Spacer(Modifier.width(5.dp))
        Button(onClick = {
            playPageTurnSound()
            onNext()
         },
            modifier = modifier,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = darkBlue // Use the loaded color for the button's background
//            )
            ) {
            Text("Next (הבא)")

        }
    }
}
    override fun onDestroy() {
        super.onDestroy()
        // Stop and release the MediaPlayer when the activity is destroyed
        if (backgroundMediaPlayer.isPlaying) {
            backgroundMediaPlayer.stop()
        }
        backgroundMediaPlayer.release()
    }

}

