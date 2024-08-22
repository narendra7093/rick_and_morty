package com.example.test

import android.net.Uri
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import coil.compose.AsyncImage
import com.example.test.Screens.Screens
import com.example.test.models.CharacterResponse
import com.example.test.models.Result

import com.example.test.ui.theme.TestTheme
import com.example.test.viewmodels.CharacterViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewmodel: CharacterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(viewmodel = viewmodel)


                }
            }
        }
    }
}

@Composable
fun MyApp(viewmodel: CharacterViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "spalsh_screen") {

        composable(route = "spalsh_screen"){
            Splash_Screen(navController = navController)
        }

        composable(Screens.Mainscreen.route) {
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Pokemon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(3.dp))
                SearchScreenUi(
                    hint = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                chardata(viewmodel = viewmodel, navController = navController)

            }

        }
        composable(
            "character_detail/{characterJson}",
            arguments = listOf(navArgument("characterJson") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val characterJson = backStackEntry.arguments?.getString("characterJson")
            val character = Gson().fromJson(characterJson, Result::class.java)
            CharacterDetailScreen(character)

        }


    }


}
@Composable
fun Splash_Screen(navController: NavController) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true){
        scale.animateTo(
            targetValue = 0.3f,
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = 300,
                easing = LinearOutSlowInEasing

            )
        )
        navController.navigate(Screens.Mainscreen.route)
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
        Image(painter = painterResource(id = R.drawable.splash), contentDescription = "Logo",
            modifier = Modifier.scale(scale = scale.value).size(500.dp))
    }

}

@Composable
fun SearchScreenUi(hint: String, modifier: Modifier) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)

        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}


@Composable
fun chardata(viewmodel: CharacterViewModel, navController: NavHostController) {
    val characters by viewmodel.response.observeAsState()

    LazyColumn {
        items(characters?.results ?: emptyList()) { character ->


            CharecterItemUi(
                modifier = Modifier.fillMaxSize(),
                characterResponse = character,
                navController = navController
            ) {

                var charjson = Uri.encode(Gson().toJson(character))
                navController.navigate("character_detail/$charjson")
            }
        }

    }


}

@Composable
fun CharecterItemUi(
    modifier: Modifier, characterResponse: Result,
    navController: NavHostController,

    onClick: () -> Unit,
) {
    Box(modifier = modifier
        .fillMaxSize()
        .clickable {
            onClick()
//            var charjson = Uri.encode(Gson().toJson(characterResponse))
//            navController.navigate("character_detail/$charjson")
        }) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(start = 2.dp, top = 0.dp, end = 2.dp, bottom = 3.dp)
                .border(1.dp, color = Color.LightGray)
                .clip(shape = CircleShape)
                .size(width = 70.dp, height = 90.dp)
                .clickable { onClick() }
        ) {
            AsyncImage(
                model = characterResponse.image,
                contentDescription = "rick and morty character",
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(5.dp))

            Column {
                Text(
                    text = characterResponse.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = characterResponse.species,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(modifier = Modifier.padding(bottom = 3.dp)) {
                    when (characterResponse.status) {
                        "Alive" -> Text(
                            text = characterResponse.status,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Green
                        )

                        "Dead" -> Text(
                            text = characterResponse.status,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )

                        else -> Text(
                            text = characterResponse.status,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = characterResponse.gender,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )

                }
            }


        }

    }

}


@Composable
fun CharacterDetailScreen(characterResponse: Result) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = characterResponse.image, contentDescription = "${characterResponse.name}",
            modifier = Modifier
                .size(150.dp)
                .padding(top = 25.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(3.dp))

        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(5.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Id :- #${characterResponse.id}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "Name :- ${characterResponse.name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Created Date :- ${characterResponse.created}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))

                when (characterResponse.gender) {
                    "Male" -> Text(
                        text = "Gender :- ${characterResponse.gender}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )

                    "Female" -> Text(
                        text = "Gender :- ${characterResponse.gender}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )

                    else -> Text(
                        text = "Gender :- ${characterResponse.gender}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }

                when (characterResponse.status) {
                    "Alive" -> Text(
                        text = "Status :- ${characterResponse.status}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Green
                    )

                    "Dead" -> Text(
                        text = "Status :- ${characterResponse.status}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )

                    else -> Text(
                        text = "Status :- ${characterResponse.status}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }

                Text(
                    text = "Species :- ${characterResponse.species}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Episodes ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))

                var listOfEpisodes = listOf(characterResponse.episode)


                for (i in listOfEpisodes.indices) {
                    LazyColumn {
                        items(listOfEpisodes.size) {
                            Text(text = listOfEpisodes[i].toString())
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                    }


                }

            }
        }


    }
}

