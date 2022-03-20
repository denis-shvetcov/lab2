package ru.spbstu.icc.kspt.lab2.continuewatch

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var secondsElapsed: Int = 0

    lateinit var textSecondsElapsed: TextView
    lateinit var backgroundThread: Thread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
    }

    override fun onPause() {
        super.onPause()
        Log.i("State", "State = paused")
        backgroundThread.interrupt()
    }

    override fun onResume() {
        super.onResume()
        Log.i("State", "State = resumed")
        backgroundThread = thread()
        backgroundThread.start()
    }


    @SuppressLint("SetTextI18n")
    private fun thread() = Thread {
        try {
            while (true) {
                Thread.sleep(1000)
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed)  + secondsElapsed++
                }
            }
        } catch (e: InterruptedException) {
            Log.i("Thread", "Thread was interrupted")
            return@Thread
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("preAction", "Save counter")
        outState.run {
            putInt("SECONDS_ELAPSED", secondsElapsed)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("postAction", "Insert counter")
        savedInstanceState.run {
            secondsElapsed = getInt("SECONDS_ELAPSED")
        }
    }
}
