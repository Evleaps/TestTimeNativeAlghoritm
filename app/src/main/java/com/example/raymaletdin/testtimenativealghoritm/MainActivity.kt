package com.example.raymaletdin.testtimenativealghoritm

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : Activity(), CoroutineScope {

    private val viewModelContext: Job = SupervisorJob()
    override val coroutineContext = viewModelContext + Dispatchers.Main

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private external fun calcNative(n: Int)

    private var java: TextView? = null
    private var c: TextView? = null
    private var send: Button? = null
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        java = findViewById(R.id.java)
        c = findViewById(R.id.cpp)
        editText = findViewById(R.id.editText)
        send = findViewById(R.id.send)

        val processDialog = CustomProgressDialog.make(this)

        send?.setOnClickListener {
            this.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    processDialog.show()
                }

                val count = editText?.text.toString().toInt()
                var timeJava = 0.0
                var timeCPP = 0.0
                try {
                    val t1 = System.currentTimeMillis()
                    calcJava(count)
                    val t2 = System.currentTimeMillis()
                    calcNative(count)
                    val t3 = System.currentTimeMillis()
                    timeJava = (t2 - t1).toDouble() / 1000
                    timeCPP = (t3 - t2).toDouble() / 1000
                } catch (e: OutOfMemoryError) {
                    withContext(Dispatchers.Main) {
                        Toast
                            .makeText(this@MainActivity, "Ошибка. Попробуй другое число", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                withContext(Dispatchers.Main) {
                    java?.text = "java = ${timeJava.toString().subSequence(0, 3)}"
                    c?.text = "c++ = ${timeCPP.toString().subSequence(0, 3)}"
                    processDialog.dismiss()
                }
            }
        }
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }

    private fun calcJava(n: Int) {
        val arr = DoubleArray(n)

        for (i in 0 until n) {
            if (i == 0 || i == 1)
                arr[i] = 1.0
            else
                arr[i] = arr[i - 1] + arr[i - 2]
        }

        for (i in 1 until n) {
            arr[i] /= arr[i - 1]
            for (k in 1 until n) {
                arr[k] *= arr[k - 1]
            }
        }
    }
}
