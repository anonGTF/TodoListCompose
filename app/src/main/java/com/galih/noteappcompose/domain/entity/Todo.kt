package com.galih.noteappcompose.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: Date,
    val isFinished: Boolean
) : Parcelable
