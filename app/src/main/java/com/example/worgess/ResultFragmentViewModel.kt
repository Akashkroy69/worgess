package com.example.worgess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultFragmentViewModel(finalResult: String) : ViewModel() {
    var finalResultDisplay :String = finalResult
}