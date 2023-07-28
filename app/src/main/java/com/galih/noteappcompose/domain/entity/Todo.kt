package com.galih.noteappcompose.domain.entity

import java.util.Date

data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: Date,
    val isFinished: Boolean
)
