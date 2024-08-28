package com.moniapps.dictinonary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.moniapps.dictinonary.domain.model.Meaning
import com.moniapps.dictinonary.domain.model.WordItem
import com.moniapps.dictinonary.presentation.MainState
import com.moniapps.dictinonary.presentation.MainUIEvents
import com.moniapps.dictinonary.presentation.MainViewModel
import com.moniapps.dictinonary.ui.theme.DictinonaryTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val keyboardController = LocalSoftwareKeyboardController.current
            var isError by remember {
                mutableStateOf(false)
            }
            DictinonaryTheme {
                BarColor()
                val mainViewModel = hiltViewModel<MainViewModel>()
                val mainState by mainViewModel.mainState.collectAsState()
                var buttonVisible by remember {
                    mutableStateOf(false)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            value = mainState.searchWord,
                            onValueChange = {
                                mainViewModel.onEvent(
                                    MainUIEvents.OnSearchWordChange(it)

                                )
                                isError = false
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = getString(R.string.search_a_word),
                                    tint = if (mainState.searchWord.isNotEmpty())
                                        MaterialTheme.colorScheme.primary
                                    else Color.Gray,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clickable {
                                            if (mainState.searchWord.isNotEmpty()) {
                                                mainViewModel.onEvent(MainUIEvents.OnSearchClicked)
                                                keyboardController?.hide()
                                                buttonVisible = true
                                            } else {
                                                isError = true
                                                Toast
                                                    .makeText(
                                                        this,
                                                        "Enter a word to search",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                        }


                                )
                            }, isError = isError,
                            label = {
                                Text(
                                    text = if(!isError) getString(R.string.search_a_word)
                                    else "Please enter a word!",
                                    fontSize = 15.sp,
                                    modifier = Modifier.alpha(0.7f)
                                )
                            },
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 19.5.sp
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (mainState.searchWord.isNotEmpty()) {
                                        mainViewModel.onEvent(MainUIEvents.OnSearchClicked)
                                        keyboardController?.hide()
                                        buttonVisible = true
                                    }


                                }
                            ),
                        )
                    },
                    content = { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = paddingValues.calculateTopPadding())
                        ) {
                            MainScreen(mainState = mainState, buttonVisible = buttonVisible)
                        }

                    }
                )
            }
        }
    }

    @Composable
    fun MainScreen(
        mainState: MainState,
        buttonVisible: Boolean
    ) {
        val context = LocalContext.current
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 24.dp, top = 8.dp)
            ) {
                mainState.wordItem?.let { wordItem ->

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = wordItem.word,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = wordItem.phonetic,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }


            }
            Column(
                modifier = Modifier.padding(end = 24.dp, top = 40.dp)
            ) {
                mainState.wordItem?.phonetics.let {
                    val url = it?.get(0)?.audio
                    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
                    var isPlaying by remember {
                        mutableStateOf(false)
                    }
                    if (url != null) {
                        ExoPlayerView(url = url, exoPlayer = exoPlayer, isPlaying = isPlaying)
                    }
                    IconButton(
                        onClick = {
                            exoPlayer.play()
                            isPlaying = true


                        }
                    ) {

                        if (buttonVisible) {

                            exoPlayer.addListener(object : Player.Listener {
                                @Deprecated("Deprecated in Java")
                                override fun onPlayerStateChanged(
                                    playWhenReady: Boolean,
                                    playbackState: Int
                                ) {
                                    super.onPlayerStateChanged(playWhenReady, playbackState)
                                    if (playbackState == Player.STATE_ENDED) {
                                        isPlaying = false
                                    }
                                }
                            })
                            if (!isPlaying)
                                Icon(
                                    painter = painterResource(id = R.drawable.play_24),
                                    contentDescription = "Play button",
                                    modifier = Modifier
                                        .size(80.dp)
                                )
                            else
                                Icon(
                                    painter = painterResource(id = R.drawable.circle_24),
                                    contentDescription = "Pause button",
                                    modifier = Modifier
                                        .size(80.dp)
                                )
                        }
                    }

                }
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 120.dp)
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp
                    )
                )
                .background(
                    MaterialTheme.colorScheme.secondaryContainer.copy(0.7f)
                )
        ) {
            if (mainState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                mainState.wordItem?.let { wordItem ->
                    WordResult(wordItem)
                }
            }
        }
    }


    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun MainScreenPreview() {
        MainScreen(
            mainState = MainState(isLoading = false, searchWord = "Meet"),
            buttonVisible = false
        )
    }

}

@Composable
fun ExoPlayerView(url: String, exoPlayer: ExoPlayer, isPlaying: Boolean) {
    val mediaSource = remember(url) { MediaItem.fromUri(url) }
    if (isPlaying)
        LaunchedEffect(isPlaying) {
            exoPlayer.setMediaItem(mediaSource)
            exoPlayer.prepare()
        }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

}


@Composable
fun WordResult(wordItem: WordItem) {
    LazyColumn(contentPadding = PaddingValues(vertical = 32.dp)) {
        items(wordItem.meanings.size) { index ->
            Meaning(
                meaning = wordItem.meanings[index],
                index = index
            )
            Spacer(modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun Meaning(
    meaning: Meaning,
    index: Int
) {
    Text(
        text = "${index + 1}. ${meaning.partOfSpeech}",
        fontSize = 17.sp,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 2.dp, bottom = 4.dp, start = 12.dp, end = 12.dp
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(0.7f),
                        Color.Transparent
                    ),
                )
            )
            .padding(
                top = 2.dp, bottom = 4.dp, start = 24.dp, end = 12.dp
            ),
    )
    Text(
        text = meaning.definition.definition,
        modifier = Modifier.padding(start = 24.dp, end = 12.dp)
    )
}

@Composable
private fun BarColor() {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colorScheme.background
    LaunchedEffect(color) {
        systemUiController.setSystemBarsColor(color)
    }
}