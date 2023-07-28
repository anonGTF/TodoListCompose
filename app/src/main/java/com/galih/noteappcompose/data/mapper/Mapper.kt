package com.galih.noteappcompose.data.mapper

interface Mapper<F, T> {
    fun mapFromEntity(data: T): F
    fun mapFromModel(data: F): T
}