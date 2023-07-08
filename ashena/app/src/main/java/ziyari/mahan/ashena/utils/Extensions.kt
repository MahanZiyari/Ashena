package ziyari.mahan.ashena.utils

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import contacts.core.entities.Contact
import contacts.core.util.emails
import contacts.core.util.groupMemberships
import contacts.core.util.names
import contacts.core.util.options
import contacts.core.util.phones
import ziyari.mahan.ashena.data.models.ContactEntity

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

fun String.removeSpaces(): String {
    return this.replace("\\s".toRegex(), "")
}

fun Contact.toEntity(): ContactEntity {
    val number = if (this.phones().toList().isEmpty()) "" else this.phones().toList()
        .first().number

    val email = if (this.emails().toList().isEmpty()) "" else this.emails().toList().first().primaryValue

    return ContactEntity(
        id = this.id.toInt(),
        firstName = this.names().first().givenName ?: "",
        lastName = this.names().first().familyName ?: "",
        profilePicture = this.photoUri.toString(),
        number = number ?: "",
        email = email ?: "Null",
        lookupKey = this.lookupKey,
        favorites = this.options()?.starred ?: false,
        isFromPhone = true,
    )
}