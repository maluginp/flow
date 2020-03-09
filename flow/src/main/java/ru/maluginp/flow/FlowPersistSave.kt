package ru.maluginp.flow

interface FlowPersistSave<in Input> {
    fun save(input: Input)
}