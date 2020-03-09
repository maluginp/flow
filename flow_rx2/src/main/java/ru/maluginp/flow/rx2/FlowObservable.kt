package ru.maluginp.flow.rx2

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import ru.maluginp.flow.Flow
import ru.maluginp.flow.FlowTarget
import ru.maluginp.flow.FlowTargetError
import ru.maluginp.flow.FlowTargetSuccess
import java.util.concurrent.CancellationException

class FlowObservable<in Input, out Output>(
    private val observable: Observable<Output>
) : Flow<Input, Output> {

    private val blockingObserver = BlockingObserver<Output>()

    override fun execute(input: Input): FlowTarget<Output> {
        observable.blockingSubscribe(blockingObserver)
        return blockingObserver.flowFirst()
    }

    override fun cancel() {
        blockingObserver.cancel()
    }
}


class BlockingObserver<T> : Observer<T> {
    var disposable: Disposable? = null
    private val nexts: MutableList<T> = mutableListOf()
    private var error: Throwable? = null


    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onNext(t: T) {
        nexts.add(t)
    }

    override fun onError(e: Throwable) {
        error = e
    }

    fun cancel() {
        error = CancellationException()
        disposable?.let { it.dispose() }
    }

    fun flowFirst(): FlowTarget<T> {
        return error?.let {
            FlowTargetError<T>(it)
        } ?: FlowTargetSuccess(nexts[0])
    }
}