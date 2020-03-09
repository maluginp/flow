package ru.maluginp.flow

interface FlowMapping<in Input, out Output> {
    fun map(input: Input): Output
}