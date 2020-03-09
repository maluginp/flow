package ru.maluginp.flow

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FlowUseCase<in Input, out Output>(
    private val executor: ExecutorService = Executors.newFixedThreadPool(
        1
    ),
    private val flow: Flow<Input, Output>,
    private val completion: (FlowTarget<Output>) -> Unit
) {
    private val handler = Handler(Looper.getMainLooper())

    fun execute(input: Input) {
        executor.submit {
            val res = flow.execute(input)

            handler.post {
                completion(res)
            }
        }
    }

    fun cancel() {
        flow.cancel()
    }
}