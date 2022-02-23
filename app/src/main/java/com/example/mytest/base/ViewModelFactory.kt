package com.example.mytest.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytest.repository.TrendingRepository
import com.example.mytest.viewmodels.TrendingViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TrendingViewModel::class.java) ->
                TrendingViewModel(repository as TrendingRepository) as T
          else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }

}