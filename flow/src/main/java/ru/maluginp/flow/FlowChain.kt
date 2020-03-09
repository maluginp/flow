package ru.maluginp.flow

//class RxFlowChain(private val steps: Iterable<RxFlow>) {
//
//}
class FlowChain<in Input, Middle, out Output>(
    private val validation: FlowValidation<Input> = NoFlowValidation(),
    private val inputMap: FlowMapping<Input, Middle>,
    private val steps: Iterable<FlowChainStep<Middle>>,
    private val outputMap: FlowMapping<Middle, Output>
) : Flow<Input, Output> {

    override fun execute(input: Input): FlowTarget<Output> {
        try {
            validation.validate(input)

            var middle = inputMap.map(input)

            for (step in steps) {
                val flowOutput = step.execute(middle)

                if (flowOutput.error != null) {
                    return FlowTargetError(flowOutput.error)
                } else {
                    middle = flowOutput.data?.let { it } ?: throw NullPointerException()
                }
            }

            val output = outputMap.map(middle)

            return FlowTargetSuccess(output)
        } catch (e: Exception) {
            return FlowTargetError(e)
        }
    }

    override fun cancel() {
        TODO("not implemented")
    }
}