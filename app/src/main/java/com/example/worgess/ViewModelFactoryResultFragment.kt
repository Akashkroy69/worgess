package com.example.worgess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ViewModelFactoryResultFragment(private val finalResult: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultFragmentViewModel::class.java))
            return ResultFragmentViewModel(finalResult) as T

        throw IllegalArgumentException("Unknown ViewModel")

    }
}