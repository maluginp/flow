package ru.maluginp.flow

class NoFlowMapping<Input> : FlowMapping<Input, Input> {
    override fun map(input: Input): Input {
        return input
    }
}