package ru.maluginp.flow

class FlowChainSingleStep<Step, in InputFlow, in OutputFlow>(
    private val validation: FlowValidation<Step> = NoFlowValidation(),
    private val inputMap: FlowMapping<Step, InputFlow>,
    private val flow: Flow<InputFlow, OutputFlow>,
    private val outputMap: FlowMapping<OutputFlow, Step>
) : FlowChainStep<Step> {
    override fun execute(input: Step): FlowTarget<Step> {
        try {
            validation.validate(input)

            val flowInput = inputMap.map(input)

            val flowOutput = flow.execute(flowInput)

            if (flowOutput.error != null) {
                return FlowTargetError(flowOutput.error)
            } else if (flowOutput.data != null) {
                return FlowTargetSuccess(outputMap.map(flowOutput.data!!))
            }

            return FlowTargetError(NullPointerException())
        } catch (e: Exception) {
            return FlowTargetError(e)
        }
    }

    override fun cancel() {
        TODO("not implemented")
    }
}