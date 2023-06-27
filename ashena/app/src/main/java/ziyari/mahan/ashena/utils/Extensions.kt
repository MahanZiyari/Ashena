package ziyari.mahan.ashena.utils

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

fun Spinner.setUpListWithAdapter(items: List<String>, onItemSelected: (String) -> Unit) {
    val adapter = ArrayAdapter<String>(context, R.layout.simple_spinner_item, items)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    this.adapter = adapter
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(items[position])
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
}

fun String.extractNumbers(): Int {
    val regex = Regex("[^0-9]")
    return regex.replace(this, "").toInt()
}