package com.example.composelayoutscodelab.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composelayoutscodelab.model.City

class MainViewModel : ViewModel() {

    private var _cities  = MutableLiveData(mutableListOf<City>())
    val cities : LiveData<MutableList<City>> = _cities

    val citiesList = mutableListOf(City("Dublin", "Ireland"), City("London", "England"), City("New York", "US"), City("Mumbai", "India"))

    init{
        _cities.value = citiesList
    }

    fun addCity(city: City){
        var currList = _cities.value
        if (currList != null) {
            currList.add(city)
        } else{
            currList = mutableListOf()
        }
        _cities.value = currList
    }

    fun removeCity(city: City){
        var currList = _cities.value
        if (currList != null) {
            currList.remove(city)
        } else{
            currList = mutableListOf()
        }
        _cities.value = currList
    }

}