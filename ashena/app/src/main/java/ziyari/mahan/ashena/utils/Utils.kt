package ziyari.mahan.ashena.utils

fun checkForEmptyString(vararg strings: String): Boolean {
    strings.forEach {
        if (it.isEmpty() || it.isBlank()) return false
    }
    return true
}