package ru.maluginp.flow

class FlowDataIterable<in Input, WebModel, DbModel, out Output>(
    private val validation: FlowValidation<Input>,
    private val receive: Flow<Input, Iterable<WebModel>>,
    private val cache: FlowCache<Input, Iterable<Output>>,
    private val persist: FlowPersist<WebModel, DbModel, Output>,
    private val fetch: FlowPersistFetch<Input, Iterable<Output>>,
    private val options: FlowDataOptions = FlowDataOptions()
) : Flow<Input, Iterable<Output>> {
    override fun execute(input: Input): FlowTarget<Iterable<Output>> {
        try {
            validation.validate(input)

            if (!options.force && cache.contains(input)) {
                return FlowTargetSuccess(cache.data(input))
            }
            val webModelTarget = receive.execute(input)

            if (webModelTarget.error != null) {
                return FlowTargetSuccess(fetch.fetch(input))
            } else {
                val outputs = mutableListOf<Output>()
                val data = webModelTarget.data!!

                for (item in data) {
                    val res = persist.execute(item)

                    if (res.data != null) {
                        outputs.add(res.data!!)
                    }
                }

                cache.store(input, outputs)

                return FlowTargetSuccess(outputs)
            }

        } catch (e: Exception) {
            return FlowTargetError(e)
        }
    }

    override fun cancel() {
        TODO("not implemented")
    }
}