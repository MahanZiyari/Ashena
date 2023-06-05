package ziyari.mahan.ashena.utils

import android.graphics.Color
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
    return String.format("#%06X", 0xFFFFFF and color)
}