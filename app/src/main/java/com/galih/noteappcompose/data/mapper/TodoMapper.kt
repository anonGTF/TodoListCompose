package com.galih.noteappcompose.data.mapper

import com.galih.noteappcompose.data.model.TodoModel
import com.galih.noteappcompose.domain.entity.Todo

class TodoMapper : Mapper<TodoModel, Todo> {
    override fun mapFromEntity(data: Todo): TodoModel =
        TodoModel(
            id = data.id,
            title = data.title,
            description = data.description,
            dueDate = data.dueDate,
            isFinished = data.isFinished
        )

    override fun mapFromModel(data: TodoModel): Todo =
        Todo(
            id = data.id,
            title = data.title,
            description = data.description,
            dueDate = data.dueDate,
            isFinished = data.isFinished
        )
}