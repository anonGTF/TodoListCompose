package com.galih.noteappcompose.domain.usecase

abstract class UseCase <T, in P> where T : Any {

    abstract fun run(params: P): T

    operator fun invoke(params: P): T = run(params)

    class None
}