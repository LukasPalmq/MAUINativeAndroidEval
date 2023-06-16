package com.example.androidkotlin_exjobb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidkotlin_exjobb.R

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val startTime = intent.getLongExtra("startTime", 0)
        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        Toast.makeText(this, "${endTime - startTime} ms", Toast.LENGTH_LONG).show()
    }
}