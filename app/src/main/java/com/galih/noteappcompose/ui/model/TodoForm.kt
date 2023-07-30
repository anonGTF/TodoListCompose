package com.galih.noteappcompose.ui.model

import me.naingaungluu.formconductor.annotations.Form
import me.naingaungluu.formconductor.annotations.MinLength

@Form
data class TodoForm(
    @MinLength(3)
    val title: String = "",
    @MinLength(3)
    val description: String = "",
)
