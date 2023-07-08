package ziyari.mahan.ashena.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Log
import java.io.IOException
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

fun getBitmapFromUri(context: Context,uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}