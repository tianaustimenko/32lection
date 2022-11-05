package com.example.lecture32

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity() , CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonCalculate = findViewById<Button>(R.id.buttonCalculate)
        buttonCalculate.setOnClickListener {
            launch {
                val loadingProgressBar =
                    findViewById<ProgressBar>(R.id.progressBar)
                loadingProgressBar.post {
                    loadingProgressBar.visibility = View.VISIBLE
                }
                calculateFactorial()
                loadingProgressBar.post {
                    loadingProgressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    suspend fun calculateFactorial() {
        var result = GlobalScope.async {
            val numberEditText = findViewById<EditText>(R.id.editTextNumber)
            factorial(numberEditText.text.toString().toInt())
        }
        GlobalScope.launch {
            val resultTextView = findViewById<TextView>(R.id.textViewResult)
            resultTextView.text = "Result: ${result.await()}"
        }
        result.join()
    }

    suspend fun factorial(num: Int): Long {
        var result: Long = 1
        for (i in 2..num) {
            result *= i
            delay(500)
        }
        return result
    }
}
