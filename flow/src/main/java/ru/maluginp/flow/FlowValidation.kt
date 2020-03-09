package ru.maluginp.flow

interface FlowValidation<in Input> {
    fun validate(input: Input)
}

class NoFlowValidation<in Input> : FlowValidation<Input> {
    override fun validate(input: Input) {}
}
