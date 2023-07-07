package ziyari.mahan.ashena.utils

import android.graphics.Color
import android.util.Log
import java.util.Random

fun checkForEmptyString(vararg strings: String): Boolean {
    strings.forEach {
        if (it.isEmpty() || it.isBlank()) return false
    }
    return true
}


fun generateRandomColor(): String {
    val random = Random()
    val r = random.nextInt(256)
    val g = random.nextInt(256)
    val b = random.nextInt(256)
    val color = Color.rgb(r, g, b)
    return String.format("%06X", 0xFFFFFF and color)
}
fun generateRandomBlue(): Int {
    val random = Random()
    val r = random.nextInt(10)
    val g = random.nextInt(25)
    val b = random.nextInt(256)
    val color = Color.rgb(r, g, b)
    return color
}

fun showDebugLog(message: String) {
    Log.i(DEBUG_TAG, message)
}