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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                        ArtPiece(R.drawable.pic5, R.string.pic5_string, R.raw.pic5_sound),
                        ArtPiece(R.drawable.pic6, R.string.pic6_string, R.raw.pic6_sound),
                        ArtPiece(R.drawable.pic7, R.string.pic7_string, R.raw.pic7_sound),
                        ArtPiece(R.drawable.pic8, R.string.pic8_string, R.raw.pic8_sound),
                        ArtPiece(R.drawable.pic9, R.string.pic9_string, R.raw.pic9_sound),
                        ArtPiece(R.drawable.pic10, R.string.pic10_string, R.raw.pic10_sound),
                        ArtPiece(R.drawable.pic11, R.string.pic11_string, R.raw.pic11_sound),
                        ArtPiece(R.drawable.pic12, R.string.pic12_string, R.raw.pic12_sound),
                        ArtPiece(R.drawable.pic13, R.string.pic13_string, R.raw.pic13_sound),
                        ArtPiece(R.drawable.pic14, R.string.pic14_string, R.raw.pic14_sound),
                        ArtPiece(R.drawable.pic15, R.string.pic15_string, R.raw.pic15_sound),
                        ArtPiece(R.drawable.pic16, R.string.pic16_string, R.raw.pic16_sound),
                        ArtPiece(R.drawable.pic17, R.string.pic17_string, R.raw.pic17_sound),
                        ArtPiece(R.drawable.pic18, R.string.pic18_string, R.raw.pic18_sound),
                        ArtPiece(R.drawable.pic19, R.string.pic19_string, R.raw.pic19_sound),
                        ArtPiece(R.drawable.pic20, R.string.pic20_string, R.raw.pic20_sound),
                        ArtPiece(R.drawable.pic21, R.string.pic21_string, R.raw.pic21_sound),



                    )
                    // Remember the current index
                    var currentIndex by remember { mutableStateOf(0) }

                    Column (modifier = Modifier
                        .verticalScroll(rememberScrollState())) {
                        ShowPicture(
                            imageResId = artPieces[currentIndex].imageResId,
                            contentDescriptionResId = artPieces[currentIndex].titleResId
                        )

                        //ShowTitle(artPieces[currentIndex].titleResId)
                        ShowTitle(imageId = R.drawable.paper , artText = artPieces[currentIndex].titleResId)


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
    fun ShowTitle(
        @DrawableRes imageId: Int,
        @StringRes artText: Int,
        modifier: Modifier = Modifier
    ) {

        val artData = stringResource(id = artText)
        Box(modifier = modifier) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null, // Provide a proper content description if needed
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = artData,
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .padding(16.dp) // Adjust padding as needed
                    .align(Alignment.Center)
            )
        }
    }

//
//@Composable
//fun ShowTitle(@StringRes artText: Int,
//    modifier: Modifier = Modifier
//){
//    val artData = stringResource(id = artText)
//    Column(){
//        Text(artData,
//            color = Color.White,
//            textAlign = TextAlign.Center
//            )
//
//    }
//}

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

