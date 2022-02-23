package com.example.mytest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytest.network.Resource
import com.example.mytest.repository.TrendingRepository
import com.example.mytest.models.TrendingModel
import kotlinx.coroutines.launch

class TrendingViewModel(private val repository: TrendingRepository) : ViewModel() {

    private val _getTrendingRepor: MutableLiveData<Resource<TrendingModel>> = MutableLiveData()
    val getTrendingRepo: LiveData<Resource<TrendingModel>>
        get() = _getTrendingRepor

    fun getTrendingRepo() = viewModelScope.launch {
        _getTrendingRepor.value = repository.getTrendingRepo()
    }


    val selectedList = mutableMapOf<Int, Boolean>()
    fun selectItem(id: Int, isSelected: Boolean){
        if (selectedList.contains(id)) {
            selectedList.remove(id)
        } else{
            selectedList.put(id, isSelected)
        }
    }


}