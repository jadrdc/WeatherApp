package com.plugin.conventions.weather.base

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.plugin.conventions.weather.base.BaseViewModel
import com.plugin.conventions.weather.base.ViewModelState
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

open class BaseActivity : ComponentActivity() {


    val <S : ViewModelState> BaseViewModel<S, *>.currentState
        get() = state.value
}

