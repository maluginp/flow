package ru.maluginp.flow

interface FlowPersistFetch<in Input, out Output> {
    fun fetch(input: Input): Output
}