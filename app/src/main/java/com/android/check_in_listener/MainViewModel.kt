package com.android.check_in_listener

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var listenData = MutableLiveData<ListenData>()

    public fun listener(){

    }
}