package com.example.composelayoutscodelab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.*
import coil.transform.CircleCropTransformation
import com.example.composelayoutscodelab.model.City
import com.example.composelayoutscodelab.nav.Screen
//import com.example.composelayoutscodelab.playground.TimeShifted
import com.example.composelayoutscodelab.ui.theme.ComposeLayoutsCodelabTheme
import com.example.composelayoutscodelab.viewmodels.MainViewModel
import com.google.accompanist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Time
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Crossfade
        setContent {
            ComposeLayoutsCodelabTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LayoutsCodelab(mainViewModel)
                }
            }
        }
    }
}


@Composable
fun LayoutsCodelab(mvm: MainViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var nameInputValue by rememberSaveable { mutableStateOf("") }

    //list of Screens for ModalDrawer Navigation
    val navItems = listOf(
        Screen.Greeting,
        Screen.PersonalizedGreeting,
        Screen.Cities,
        Screen.NumberList,
        Screen.LazyList,
        Screen.ImageListScreen,
        //Screen.TimeShiftedScreen
        Screen.MyOwnColumn,
        Screen.StaggeredChips,
        Screen.SimpleConstraint
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
        drawerContent = {
            MyDrawer(
                items = navItems,
                navController = navController,
                myDrawerState = drawerState,
                myScope = scope
            )
        }
    )
    { innerPadding ->
        NavHost(navController, startDestination = Screen.Greeting.route) {
            composable(Screen.Greeting.route) {
                Greeting(Modifier.padding(innerPadding),
                    navController,
                    nameInputValue,
                    onNameChange = { nameInputValue = it })
            }
            composable(Screen.PersonalizedGreeting.route) { navBackStackEntry ->

                navBackStackEntry.arguments?.getString("name")?.let {
                    Log.d(
                        "mainAct", it
                    )
                }
                NewBodyContent(
                    Modifier.padding(innerPadding),
                    navController,
                    navBackStackEntry.arguments?.getString("name", "empty name")
                )
            }
            composable(Screen.Cities.route) {

                CitiesScreen(
                    Modifier.padding(innerPadding),
                    mvm
                )
            }
            composable(Screen.NumberList.route) {
                SimpleList()
            }
            composable(Screen.LazyList.route) {
                LazyList()
            }
            composable(Screen.ImageListScreen.route) {
                ImageList()
            }
//            composable(Screen.TimeShiftedScreen.route){
//                TimeShifted()
//            }
            composable(Screen.MyOwnColumn.route) {
                MyOwnColContent()
            }
            composable(Screen.StaggeredChips.route) {
                StaggeredBodyContent(rows = 5, mvm = mvm)
            }
            composable(Screen.SimpleConstraint.route){
                ComposeLayoutsCodelabTheme {
                    ConstraintLayoutContent()
                }
            }
        }
    }
}

@Composable
fun StaggeredBodyContent(modifier: Modifier = Modifier, rows: Int, mvm: MainViewModel) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {

        StaggeredGrid(modifier = modifier, rows = rows) {
            for (topic in mvm.topics)
                Chip(modifier = Modifier.padding(8.dp), text = topic)
        }
    }
}

@Composable
fun ConstraintLayoutContent(){
    ConstraintLayout() {
        val (button1, button2, text) = createRefs()

        Button(
            onClick = {
                Log.d("constraint", "button1 clicked")
            },
            modifier = Modifier.constrainAs(button1){
                top.linkTo(parent.top, margin = 16.dp)
            }
        ){
            Text(text = "Button1")
        }
        Text("Text",
            Modifier
                .background(colorResource(id = android.R.color.holo_green_light))
                .constrainAs(text) {
                    top.linkTo(button1.bottom, margin = 16.dp)
                    centerAround(button1.end)
                })
        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = {Log.d("constraint", "button2 clicked")},
            modifier = Modifier.constrainAs(button2){
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ){
            Text("Button 2")
        }
    }
}

@Composable
fun LargeConstraintLayout(){
    ConstraintLayout() {
        val text = createRef()

        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text("This is a very very very very very very very very long text",
        Modifier.constrainAs(text){
            linkTo(start = guideline, end = parent.end)
            width = Dimension.preferredWrapContent
        })
    }
}

@Preview
@Composable
fun LargeConstrainLayoutPreview(){
    ComposeLayoutsCodelabTheme {
        LargeConstraintLayout()
    }
}

@Composable
fun CitiesScreen(modifier: Modifier = Modifier, vm: MainViewModel) {
    val cities: List<City> by vm.cities.observeAsState(initial = listOf())
    LazyColumn(modifier = modifier) {
        items(items = cities) { city ->
            CityItem(city)
            Divider(color = Color.Black)
        }
    }
}

@Composable
fun CityItem(city: City) {
    Column() {
        Text(city.name)
        Text(city.country)
    }
}

@Composable
fun SimpleList() {
    //passing scrollState to Modifier of Column required for scrolling
    val scrollState = rememberScrollState()

    //Column renders all items, regardless of presence on screen
    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text("Item #$it")
            Divider(modifier = Modifier.background(color = Color.Black))
        }
    }
}

@Composable
fun LazyList() {
    //RememberLazyListState necessary for scrolling
    //also useful for programmatic scrolling
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(100) {
            Text("Item #$it")
        }
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable: Measurable ->
            //measure each child
            measurable.measure(constraints)
        }
        //track y coord we have placed children up to
        var yPosition = 0
        layout(constraints.maxWidth, constraints.maxHeight) {
            //place children
            placeables.forEach { placeable ->
                //Position item on screen
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }

    }
}

@Composable
fun MyOwnColContent(modifier: Modifier = Modifier) {
    MyOwnColumn(modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("we've done it by hand!")
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        //track width of each row
        val rowWidths = IntArray(rows) { 0 }

        //track maxHeight of each row
        val rowMaxHeights = IntArray(rows) { 0 }

        //list of measured children, given constraints
        val placeables = measurables.mapIndexed { index, measureable ->
            val placeable = measureable.measure(constraints)

            //track width and maxHeight
            val row = index % rows
            rowWidths[row] = rowWidths[row] + placeable.width.absoluteValue
            rowMaxHeights[row] = kotlin.math.max(rowMaxHeights[row], placeable.height.absoluteValue)

            placeable
        }

        //grid's width is widest row
        val width =
            rowWidths.maxOrNull()?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth))
                ?: constraints.minWidth

        //Grid's height is sum of the tallest el in each row
        //coerced to height constraints
        val height = rowMaxHeights.sumBy { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        //Y of each row, mased on height accumulation of prev rows
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowMaxHeights[i - 1]
        }

        //set size of parent layout
        layout(width, height) {
            //x cord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}


@Preview
@Composable
fun ChipPreview() {
    ComposeLayoutsCodelabTheme() {
        Chip(text = "Hi there!")
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CoilImage(
            data = "https://www.freepik.com/download-file/1166230",
            contentDescription = "Android Logo",
            requestBuilder = {
                transformations(CircleCropTransformation())
            },
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Crop,
            loading = {
                Box(Modifier.matchParentSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.TopStart))
                }
            },
            error = {
                Log.d("MainAct:CoilImage", "throwable: ${it.throwable}")
                Image(
                    painterResource(R.drawable.ic_error_foreground),
                    "standin string. is this contentDesc?"
                )
            }
        )
        Spacer(Modifier.width(10.dp))
        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun ImageList() {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val listCount = 100

    Column {
        Row {
            Button(onClick = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }
            ) { Text("Scroll to top") }
            Button(onClick = {
                scope.launch {
                    //scroll to last index of list
                    scrollState.animateScrollToItem(listCount - 1)
                }
            }) { Text("Scroll to end") }
        }

        LazyColumn(state = scrollState) {
            items(listCount) {
                ImageListItem(index = it)
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    navController: NavController,
    inputValue: String,
    onNameChange: (String) -> Unit,
) {
    Column(modifier = modifier) {

        TextField(inputValue, onValueChange = onNameChange,
            placeholder = { Text(text = "please enter your name") })
        Button(onClick = {
            Log.d("mainAct", inputValue)

            navController.navigate(
                if (inputValue.isNotEmpty()) Screen.PersonalizedGreeting.route.replace(
                    "{name}",
                    inputValue
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
fun NewBodyContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    name: String?
) {
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
fun MyDrawer(
    modifier: Modifier = Modifier,
    items: List<Screen>,
    navController: NavController,
    myDrawerState: DrawerState,
    myScope: CoroutineScope
) {
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
                                colorResource(id = android.R.color.background_light)
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

//@Preview(showBackground = true)
//@Composable
//fun LayoutsCodelabPreview() {
//    ComposeLayoutsCodelabTheme {
//        LayoutsCodelab()
//    }
//}

@Preview(showBackground = true)
@Composable
fun TextWithPaddingToBaselinePreview() {
    ComposeLayoutsCodelabTheme() {
        Text("Hi There!", Modifier.firstBaselineToTop(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TextWithNormalPadding() {
    ComposeLayoutsCodelabTheme() {
        Text("Hi There!", Modifier.padding(top = 32.dp))
    }
}

private fun Modifier.firstBaselineToTop(firstBaselineToTop: Dp) =
    Modifier.layout { measureable, constraints ->
        val placeable = measureable.measure(constraints)

        //check composable has first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        //Height of composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            placeable.placeRelative(0, placeableY)
        }
    }
