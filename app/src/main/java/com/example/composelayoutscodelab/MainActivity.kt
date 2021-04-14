package com.example.composelayoutscodelab

import android.os.Bundle
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composelayoutscodelab.ui.theme.ComposeLayoutsCodelabTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        drawerContent = {MyDrawer(myDrawerState = drawerState, myScope = scope)}
    )
    { innerPadding ->
        BodyContent(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
        )
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
fun MyDrawer(modifier: Modifier = Modifier, myDrawerState: DrawerState, myScope: CoroutineScope){
    ModalDrawer(drawerState = myDrawerState,
    drawerContent = {
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp),
            onClick = { myScope.launch { myDrawerState.close() } },
            content = { Text("Close Drawer") }
        )
    },
    content = {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
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
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text("Hi there!")
        Text("Thanks for going through the codelab")

    }
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