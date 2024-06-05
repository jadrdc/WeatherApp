package com.plugin.conventions.weather.base


import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KProperty1


/**
 * The Base ViewModel class your ViewModel should inherit from
 *
 */
abstract class BaseViewModel<S : ViewModelState, E : Any>(
    initialState: S
) : ViewModel() {

    private val _state = mutableStateOf(initialState)
    val state: MutableState<S>
        get() = _state

    val events: Flow<E>
        get() = _events.receiveAsFlow()

    /*
     * Never ever change the channel size, it might lead to unpredicted behavior.
     */
    private val _events = Channel<E>(Channel.UNLIMITED)
    private val stateMutex = Mutex()

    /**
     * Update the View state using a reducer function synchronously.
     *
     */
    protected suspend fun setState(reducer: S.() -> S) {
        _state.value = stateMutex.withLock {
            _state.value.reducer()
        }
    }

    /**
     * Update the View state using a reducer function asynchronously.
     *
     */
    protected fun updateState(reducer: S.() -> S) {
        viewModelScope.launch {
            setState(reducer)
        }
    }

    /**
     * Sends an Event to the Event's queue. Each event will be received one time by a single
     * listener.
     *
     */
    protected fun sendEvent(event: E) {
        _events.trySend(event).let {
            if (!it.isSuccess || it.isClosed) {
                throw MviBaseException(
                    "$it",
                    IllegalStateException(
                        "The only known way to fail with this exception is to exceed the " +
                                "capacity of the channel. The capacity is set to UNLIMITED, " +
                                "so there should be no way to fail with this exception."
                    )
                )
            }
        }
    }

    /**
     * Binds provided Flow to update ViewModel state
     */
    protected fun <T> Flow<T>.bind(reducer: S.(value: T) -> S) {
        onEach { value ->
            setState {
                reducer(value)
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Never use it: this function added for test purpose only.
     */
    internal fun closeEvents() {
        _events.close()
    }
    fun <T> Flow<T>.collectWithLifecycle(
        owner: LifecycleOwner,
        minState: Lifecycle.State = Lifecycle.State.STARTED,
        collector: FlowCollector<T>
    ) = owner.lifecycleScope.launch {
        owner.lifecycle.whenStateAtLeast(minState) {
            collect(collector)
        }
    }
}

interface ViewModelState

open class MviBaseException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)
