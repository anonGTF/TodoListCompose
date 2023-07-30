package com.galih.noteappcompose.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import com.galih.noteappcompose.util.Resource

@Composable
fun <T> Observer(
    data: LiveData<Resource<T>>,
    onSuccess: @Composable (data: T) -> Unit,
    onError: @Composable (message: String) -> Unit = { ErrorComponent(message = it) },
    onLoading: @Composable (data: T) -> Unit = { LoadingComponent() },
    onEmpty: @Composable () -> Unit = {},
) {
    data.observeAsState().value.let { result ->
        when (result) {
            is Resource.Success -> {
                if (result.data == null) {
                    onEmpty.invoke()
                    return
                }
                onSuccess.invoke(result.data)
            }

            is Resource.Error -> {
                onError.invoke(result.message.toString())
            }

            is Resource.Loading -> {
                if (result.data == null) {
                    onEmpty.invoke()
                    return
                }
                onLoading.invoke(result.data)
            }

            else -> {}
        }
    }
}