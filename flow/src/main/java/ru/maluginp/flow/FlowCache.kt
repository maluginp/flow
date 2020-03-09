package ru.maluginp.flow

interface FlowCache<in Input, Output> {
    fun contains(input: Input): Boolean
    fun data(input: Input): Output
    fun store(input: Input, output: Output)
}