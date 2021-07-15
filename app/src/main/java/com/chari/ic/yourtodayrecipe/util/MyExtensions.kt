package com.chari.ic.yourtodayrecipe.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
    observe(owner, object: Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observer.onChanged(t)
        }

    })
}