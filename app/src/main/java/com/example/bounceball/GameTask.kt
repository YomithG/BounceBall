package com.example.bounceball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

interface GameTask {
    fun closeGame(mScore:Int)

}