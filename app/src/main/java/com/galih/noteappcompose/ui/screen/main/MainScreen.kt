package com.galih.noteappcompose.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.galih.noteappcompose.domain.entity.Todo
import com.galih.noteappcompose.ui.component.Observer
import com.galih.noteappcompose.ui.model.ActionState
import com.galih.noteappcompose.ui.screen.destinations.AddScreenDestination
import com.galih.noteappcompose.ui.screen.destinations.EditScreenDestination
import com.galih.noteappcompose.util.Utils.toString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import de.palm.composestateevents.EventEffect

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val toggleState: ActionState by viewModel.toggleStateStream.collectAsState()

    EventEffect(
        event = toggleState.eventSucceed,
        onConsumed = viewModel::onConsumedToggleSucceed
    ) {
        snackbarHostState.showSnackbar("Success updating Todo")
    }

    EventEffect(
        event = toggleState.eventFailed,
        onConsumed = viewModel::onConsumedToggleFailed
    ) {
        snackbarHostState.showSnackbar(it)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Todo List") },
                modifier = Modifier.background(color = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigator.navigate(AddScreenDestination()) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Todo"
                )
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Observer(
                    data = viewModel.todoState,
                    onSuccess = {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(items = it) { todo ->
                                TodoCard(
                                    todo = todo,
                                    onClick = { navigator.navigate(EditScreenDestination(todo = todo)) },
                                    onChecked = { viewModel.toggleFinished(todo) }
                                )
                            }
                        }
                    },
                    onEmpty = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(text = "There is no todo yet")
                        }
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCard(
    todo: Todo,
    onClick: () -> Unit,
    onChecked: () -> Unit
) {
    Card(
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 3.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
           Column(
               modifier = Modifier.weight(1f)
           ) {
               Text(
                   text = todo.title,
                   fontWeight = FontWeight.Bold,
                   fontSize = 16.sp
               )
               Spacer(modifier = Modifier.height(4.dp))
               Text(text = "due: ${todo.dueDate.toString("dd MMMM yyyy")}")
           }
           Checkbox(
               checked = todo.isFinished,
               onCheckedChange = { onChecked() }
           )
        }
    }
}