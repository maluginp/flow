package ru.maluginp.flow

class FlowData<in Input, WebModel, DbModel, out Output>(
    private val validation: FlowValidation<Input>,
    private val receive: Flow<Input, WebModel>,
    private val cache: FlowCache<Input, Output>,
    private val persist: FlowPersist<WebModel, DbModel, Output>,
    private val fetch: FlowPersistFetch<Input, Output>,
    private val options: FlowDataOptions = FlowDataOptions()
) : Flow<Input, Output> {
    override fun execute(input: Input): FlowTarget<Output> {
        try {
            validation.validate(input)

            if (!options.force && cache.contains(input)) {
                return FlowTargetSuccess(cache.data(input))
            }
            val webModelTarget = receive.execute(input)

            if (webModelTarget.error != null) {
                return FlowTargetSuccess(fetch.fetch(input))
            } else {
                val persistOutput = persist.execute(webModelTarget.data!!)

                if (persistOutput.error != null) {
                    return FlowTargetError(persistOutput.error)
                } else {
                    return persistOutput.data?.let {
                        cache.store(input, it)
                        FlowTargetSuccess(it)
                    } ?: throw NullPointerException()
                }
            }


        } catch (e: Exception) {
            return FlowTargetError(e)
        }
    }

    override fun cancel() {
        TODO("not implemented")
    }
}