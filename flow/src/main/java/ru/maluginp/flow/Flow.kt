package ru.maluginp.flow

interface Flow<in Input, out Output> {
    fun execute(input: Input): FlowTarget<Output>
    fun cancel()
}





