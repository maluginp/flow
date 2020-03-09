package ru.maluginp.flow

interface FlowTarget<out Output> {
    val data: Output?
    val error: Throwable?
}

class FlowTargetSuccess<out Output>(
    override val data: Output?
) : FlowTarget<Output> {

    override val error: Throwable?
        get() = TODO("not implemented")
}


class FlowTargetError<out Output>(
    override val error: Throwable?
) : FlowTarget<Output> {
    override val data: Output?
        get() = TODO("not implemented")
}