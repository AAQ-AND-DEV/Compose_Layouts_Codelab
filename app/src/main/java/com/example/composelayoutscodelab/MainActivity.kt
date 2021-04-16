package com.example.composelayoutscodelab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.composelayoutscodelab.nav.Screen
import com.example.composelayoutscodelab.ui.theme.ComposeLayoutsCodelabTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Crossfade
        setContent {
            ComposeLayoutsCodelabTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LayoutsCodelab()
                }
            }
        }
    }
}


@Composable
fun LayoutsCodelab() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val nameInputValue = remember { mutableStateOf(TextFieldValue()) }
    //list of Screens for ModalDrawer Navigation
    val navItems = listOf(
        Screen.Greeting,
        Screen.PersonalizedGreeting
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("LayoutsCodelab")
                },
                actions = {
                    IconButton(onClick = {/* doSomething */ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                    IconButton(onClick = {/* do something else */ }) {
                        Icon(Icons.Filled.AddBusiness, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            MyBottomBar()
        },
        drawerContent = { MyDrawer(items = navItems, navController = navController, myDrawerState = drawerState, myScope = scope) }
    )
    { innerPadding ->
        NavHost(navController, startDestination = Screen.Greeting.route) {
            composable(Screen.Greeting.route) { Greeting(navController, nameInputValue) }
            composable(Screen.PersonalizedGreeting.route) { navBackStackEntry ->

                navBackStackEntry.arguments?.getString("name")?.let {
                    Log.d(
                        "mainAct", it
                    )
                }
                NewBodyContent(
                    navController,
                    navBackStackEntry.arguments?.getString("name", "empty name")
                )

            }
        }
    }
}

@Composable
fun Greeting(
    navController: NavController,
    inputValue: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier
) {
    Column() {

        TextField(inputValue.value, onValueChange = { inputValue.value = it },
            placeholder = { Text(text = "please enter your name") })
        Button(onClick = {
            Log.d("mainAct", inputValue.value.text)

            navController.navigate(
                if (inputValue.value.text.isNotEmpty()) Screen.PersonalizedGreeting.route.replace(
                    "{name}",
                    inputValue.value.text
                ) else {
                    Screen.PersonalizedGreeting.route.replace("{name}", "nameless one")
                }
            ) {
                launchSingleTop = true
            }
        }
        ) {
            Text("Navigate to Personalized Greeting")
        }
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text("Hi there!")
        Text("Thanks for going through the codelab")

    }
}

@Composable
fun NewBodyContent(navController: NavController, name: String?, modifier: Modifier = Modifier) {
    Row(modifier) {
        if (name != null) {
            Text(stringResource(R.string.other_composable_greeting, name))
        } else {
            Text(stringResource(R.string.other_composable_greeting, "nameless one"))
        }
        Text("This is another TV")
    }
}

@Composable
fun MyBottomBar(modifier: Modifier = Modifier) {
    BottomAppBar(modifier) {
        Text(
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            text = "Bottom Bar"
        )
    }
}

@Composable
fun MyDrawer(modifier: Modifier = Modifier, items: List<Screen>, navController: NavController, myDrawerState: DrawerState, myScope: CoroutineScope) {
    val navBackStaEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStaEntry?.arguments?.getString(KEY_ROUTE)
    ModalDrawer(drawerState = myDrawerState,
        gesturesEnabled = true,

        drawerContent = {
            items.forEach { screen ->
                Text(
                    stringResource(screen.resId),
                    Modifier
                        .clickable {
                            navController.navigate(screen.route) {
                                popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        }
                        .background(
                            if (currentRoute == screen.route) colorResource(id = R.color.purple_700) else {
                                colorResource(id = android.R.color.background_dark)
                            }
                        ),
                )
            }
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                onClick = { myScope.launch { myDrawerState.close() } },
                content = { Text("Close Drawer") }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = if (myDrawerState.isClosed) ">>> Swipe >>>" else "<<< Swipe <<<")
                Spacer(Modifier.height(20.dp))
                Button(onClick = { myScope.launch { myDrawerState.open() } }) {
                    Text("Click to open")
                }
            }
        })
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Row(modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colors.surface)
        .clickable { //ignoring onClick
        }
        .padding(16.dp)) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            //Img goes here
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeLayoutsCodelabTheme {
        PhotographerCard()
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutsCodelabPreview() {
    ComposeLayoutsCodelabTheme {
        LayoutsCodelab()
    }
}