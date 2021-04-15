package com.example.composelayoutscodelab.nav

import androidx.annotation.StringRes
import com.example.composelayoutscodelab.R

sealed class Screen(val route: String, @StringRes val resId: Int){
    object Greeting: Screen("greeting", R.string.greeting_route)
    object PersonalizedGreeting: Screen("personGreeting", R.string.personal_greeting_route)
}
