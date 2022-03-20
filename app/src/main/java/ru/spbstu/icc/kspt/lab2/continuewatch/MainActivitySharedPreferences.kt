package ru.spbstu.icc.kspt.lab2.continuewatch

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivitySharedPreferences : AppCompatActivity() {
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

    override fun onStart() {
        super.onStart()
        val sharedPref = this.getSharedPreferences(
            (R.string.storage.toString()),
            Context.MODE_PRIVATE
        )
        secondsElapsed = sharedPref.getInt(resources.getString(R.string.seconds_elapsed), 0)
    }

    override fun onStop() {
        super.onStop()
        val sharedPref = this.getSharedPreferences(
            (R.string.storage.toString()),
            Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putInt(resources.getString(R.string.seconds_elapsed), secondsElapsed)
            apply()
        }
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
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed) + secondsElapsed++
                }
            }
        } catch (e: InterruptedException) {
            Log.i("Thread", "Thread was interrupted")
            return@Thread
        }
    }
}
