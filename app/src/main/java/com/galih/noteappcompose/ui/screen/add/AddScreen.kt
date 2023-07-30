package com.galih.noteappcompose.ui.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.galih.noteappcompose.ui.component.DatePickerDialog
import com.galih.noteappcompose.ui.model.ActionState
import com.galih.noteappcompose.ui.model.TodoForm
import com.galih.noteappcompose.ui.screen.destinations.MainScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.palm.composestateevents.EventEffect
import de.palm.composestateevents.NavigationEventEffect
import me.naingaungluu.formconductor.FieldResult
import me.naingaungluu.formconductor.FormResult
import me.naingaungluu.formconductor.composeui.field
import me.naingaungluu.formconductor.composeui.form
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AddScreen(
    navigator: DestinationsNavigator,
    viewModel: AddViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val addState: ActionState by viewModel.addStateStream.collectAsState()

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMMM yyyy")
                .format(pickedDate)
        }
    }
    val dateDialogState = rememberMaterialDialogState()

    NavigationEventEffect(
        event = addState.eventSucceed,
        onConsumed = viewModel::onConsumedAddingSucceed
    ) {
        navigator.navigate(MainScreenDestination()) {
            popUpTo(MainScreenDestination.route) { inclusive = true }
        }
    }

    EventEffect(
        event = addState.eventFailed,
        onConsumed = viewModel::onConsumedAddingFailed
    ) {
        snackbarHostState.showSnackbar(it)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Todo") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "back icon")
                    }
                },
                modifier = Modifier.background(color = Color.White)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                form(TodoForm::class) {
                    field(TodoForm::title) {
                        OutlinedTextField(
                            value = state.value?.value.orEmpty(),
                            onValueChange = ::setField,
                            isError = resultState.value is FieldResult.Error,
                            label = { Text(text = "Title") },
                            placeholder = { Text(text = "Input your Todo title (min 3 chars)") },
                            supportingText = {
                                val result = resultState.value
                                if (result is FieldResult.Error) {
                                    Text(text = result.message)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    field(TodoForm::description) {
                        OutlinedTextField(
                            value = state.value?.value.orEmpty(),
                            onValueChange = ::setField,
                            isError = resultState.value is FieldResult.Error,
                            label = { Text(text = "Description") },
                            placeholder = { Text(text = "Input your Todo description (min 3 chars)") },
                            supportingText = {
                                val result = resultState.value
                                if (result is FieldResult.Error) {
                                    Text(text = result.message)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Pick Due Date")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedIconButton(
                            onClick = { dateDialogState.show() },
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Icon(Icons.Filled.DateRange, contentDescription = "select date icon")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = formattedDate)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val result = formState.value
                    Button(
                        enabled = formState.value is FormResult.Success,
                        onClick = {
                            if (result is FormResult.Success) {
                                viewModel.addTodo(
                                    result.data.title,
                                    result.data.description,
                                    pickedDate
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Add Todo")
                    }

                    if (addState.isLoading) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            DatePickerDialog(dialogState = dateDialogState) {
                pickedDate = it
            }
        }
    )
}