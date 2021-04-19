package com.example.composelayoutscodelab.nav

import androidx.annotation.StringRes
import com.example.composelayoutscodelab.R

sealed class Screen(val route: String, @StringRes val resId: Int){
    object Greeting: Screen("greeting", R.string.greeting_route)
    object PersonalizedGreeting: Screen("personGreeting/{name}", R.string.personal_greeting_route)
    object Cities: Screen("cities", R.string.cities_route)
    object NumberList: Screen("numbers", R.string.number_list_route)
    object LazyList: Screen("lazyList", R.string.lazy_list_route)
    object ImageListScreen: Screen("ImageList", R.string.image_list_route)
    //object TimeShiftedScreen: Screen("timeShift", R.string.time_shift_route)
    object MyOwnColumn: Screen("myOwnColumn", R.string.my_own_col_route)
    object StaggeredChips: Screen("staggeredChips", R.string.staggered_chips_route)
}
