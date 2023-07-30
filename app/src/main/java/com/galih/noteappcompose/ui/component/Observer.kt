package com.galih.noteappcompose.ui.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.galih.noteappcompose.util.Resource
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> Observer(
    data: Flow<Resource<T>>,
    onSuccess: @Composable (data: T) -> Unit,
    onError: @Composable (message: String) -> Unit = { ErrorComponent(message = it) },
    onLoading: @Composable (data: T) -> Unit = { LoadingComponent() },
    onEmpty: @Composable () -> Unit = {},
) {
    val result by data.collectAsState(initial = Resource.Loading())
    when (result) {
        is Resource.Success -> {
            Log.d("coba", "Observer: ${result.data}")
            if (result.data is List<*> && (result.data as List<*>).isEmpty()) {
                onEmpty.invoke()
                return
            }
            if (result.data == null) {
                onEmpty.invoke()
                return
            }
            onSuccess.invoke(result.data!!)
        }

        is Resource.Error -> {
            onError.invoke(result.message.toString())
        }

        is Resource.Loading -> {
            if (result.data is List<*> && (result.data as List<*>).isEmpty()) {
                onEmpty.invoke()
                return
            }
            if (result.data == null) {
                onEmpty.invoke()
                return
            }
            onLoading.invoke(result.data!!)
        }
    }
}