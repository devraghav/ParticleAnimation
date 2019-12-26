package com.rt.particleanimation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*

class TimerViewModel(app: Application) : AndroidViewModel(app) {

    private var timer: Timer = Timer()
    var timberObservable = MutableLiveData<Int>()

    fun startTimber(interval: Long = 1000) {
        timer.scheduleAtFixedRate(AddViewTimer(), 0, interval)
    }

    inner class AddViewTimer : TimerTask() {
        override fun run() {
            timberObservable.postValue( timberObservable.value?.plus(1))

        }

    }


    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}