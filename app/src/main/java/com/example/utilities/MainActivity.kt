package com.example.utilities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jdrodi.jprogress.JProgress

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JProgress.create(this, JProgress.Style.SPIN_INDETERMINATE).show()
    }
}
