package ru.maluginp.flow

open class FlowPersist<in Input, DbModel, out Output>(
    private val validation: FlowValidation<Input> = NoFlowValidation(),
    private val fetch: FlowPersistFetch<Input, DbModel>,
    private val merge: FlowPersistMerge<Input, DbModel, DbModel>,
    private val save: FlowPersistSave<DbModel>,
    private val output: FlowMapping<DbModel, Output>
) : Flow<Input, Output> {

    override fun execute(input: Input): FlowTarget<Output> {
        try {
            validation.validate(input)
            val dbModel = fetch.fetch(input)

            val dbUpdatedModel = merge.merge(input, dbModel)

            save.save(dbUpdatedModel)

            val outputModel = output.map(dbUpdatedModel)

            return FlowTargetSuccess(outputModel)
        } catch (e: Exception) {
            return FlowTargetError(e)
        }
    }

    override fun cancel() {
        TODO("not implemented")
    }
}

class FlowPersistSingle<Input>(
    private val validation: FlowValidation<Input> = NoFlowValidation(),
    private val fetch: FlowPersistFetch<Input, Input>,
    private val merge: FlowPersistMerge<Input, Input, Input>,
    private val save: FlowPersistSave<Input>
) : FlowPersist<Input, Input, Input>(
    validation, fetch, merge, save, NoFlowMapping()
)

class FlowPersistBi<in Input, Output>(
    private val validation: FlowValidation<Input> = NoFlowValidation(),
    private val fetch: FlowPersistFetch<Input, Output>,
    private val merge: FlowPersistMerge<Input, Output, Output>,
    private val save: FlowPersistSave<Output>
) : FlowPersist<Input, Output, Output>(
    validation, fetch, merge, save, NoFlowMapping()
)