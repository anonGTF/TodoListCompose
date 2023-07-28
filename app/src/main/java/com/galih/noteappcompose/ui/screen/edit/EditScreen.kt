package com.galih.noteappcompose.ui.screen.edit

import androidx.compose.runtime.Composable
import com.galih.noteappcompose.domain.entity.Todo
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun EditScreen(
    todo: Todo,
    navigator: DestinationsNavigator
) {}