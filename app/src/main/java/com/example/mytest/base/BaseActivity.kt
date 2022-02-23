package com.example.mytest.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.mytest.network.APIInterface
import com.example.mytest.network.RemoteDataSource

abstract class BaseActivity<VM : ViewModel, B : ViewBinding, R : BaseRepository> : AppCompatActivity() {

    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val apiInterface = RemoteDataSource().buildApi(APIInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getActivityBinding()
        val factory = ViewModelFactory(getActivityRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getActivityBinding(): B

    abstract fun getActivityRepository(): R
}