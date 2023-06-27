package ziyari.mahan.ashena.data.repository

import android.content.Context
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ziyari.mahan.ashena.R
import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.extractNumbers
import ziyari.mahan.ashena.utils.generateRandomColor
import ziyari.mahan.ashena.utils.removeSpaces
import javax.inject.Inject

class ContactHomeScreenRepository @Inject constructor(private val contactDao: ContactDao, @ApplicationContext  private val context: Context) {

    fun getAllContactsFromDatabase() = contactDao.getAllContacts()



    fun getAllContacts(): Flow<List<ContactEntity>> = flow {
        val contacts = mutableListOf<ContactEntity>()

        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val starred = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.STARRED)) == 1



                val contactEntity = ContactEntity(
                    id = id.extractNumbers(),
                    firstName = name,
                    favorites = starred
                )
                contacts.add(contactEntity)
            }
        }
        emit(contacts)
    }


    fun getContacts(): Flow<List<ContactEntity>> = flow {
        val contacts = mutableListOf<ContactEntity>()

        // Create a content resolver and query device contacts
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.let {
            while (it.moveToNext()){
                val id = it.getLong(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val label = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL))
                val starred = it.getInt(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.STARRED)) == 1

                val contactEntity = ContactEntity(
                        id = id.toInt(),
                        firstName = name,
                        favorites = starred,
                        number = phoneNumber,
                        group = label ?: "",
                        profilePicture = context.getString(R.string.avatar_api, name.removeSpaces(), generateRandomColor())
                )
                contacts.add(contactEntity)
            }
        }
        emit(contacts)
    }
}