package ru.maluginp.flow.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.maluginp.flow.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FlowUseCase(flow = TestUseCase()) {
            it.data
        }
    }
}

class TestUseCase : Flow<String, String> {
    override fun execute(input: String): FlowTarget<String> {
        return FlowTargetSuccess("1")
    }

    override fun cancel() {}
}
