package ziyari.mahan.ashena.data.repository

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import contacts.core.Contacts
import contacts.core.util.groupMemberships
import contacts.core.util.options
import contacts.core.util.phones
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.DEBUG_TAG
import ziyari.mahan.ashena.utils.showDebugLog
import javax.inject.Inject

class ContactHomeScreenRepository @Inject constructor(
    private val contactDao: ContactDao,
    @ApplicationContext private val context: Context
) {

    fun getAllContactsFromDatabase() = contactDao.getAllContacts()

    fun getDeviceContactsUsingReborn() = flow {
        val modelContacts = mutableSetOf<ContactEntity>()
        val contacts = Contacts(context)
            .broadQuery()
            .find()



        contacts.forEach {
            showDebugLog("Contact : ${it.displayNamePrimary} with lookup key : ${it.lookupKey}")
            val number = if (it.phones().toList().isEmpty()) "0000" else it.phones().toList()
                .first().normalizedNumber
            val group =
                if (it.groupMemberships().toList().isEmpty()) "UNGroup" else it.groupMemberships()
                    .toList().first().toString()
            modelContacts.add(
                ContactEntity(
                    id = it.id.toInt(),
                    firstName = it.displayNamePrimary ?: "Unnamed",
                    lastName = "",
                    profilePicture = it.photoThumbnailUri.toString(),
                    number = number ?: "",
                    lookupKey = it.lookupKey,
                    group = group,
                    favorites = it.options()?.starred ?: false,
                    isFromPhone = true
                )
            )
        }

        emit(modelContacts.toList())
    }


    fun getContacts(): Flow<List<ContactEntity>> = flow {
        val contacts = mutableSetOf<ContactEntity>()

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
            while (it.moveToNext()) {
                val id =
                    it.getLong(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val label =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL))
                val starred =
                    it.getInt(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.STARRED)) == 1

                val thumbnailUriString =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                val lookupKey =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY))


                val contactEntity = ContactEntity(
                    id = id.toInt(),
                    lookupKey = lookupKey,
                    isFromPhone = true,
                    firstName = name,
                    favorites = starred,
                    number = phoneNumber,
                    group = label ?: "",
                    profilePicture = thumbnailUriString ?: ""
                    /*profilePicture = context.getString(
                        R.string.avatar_api,
                        name.removeSpaces(),
                        generateRandomColor()
                    )*/
                )
                Log.i(DEBUG_TAG, "entity: $name == $lookupKey")
                contacts.add(contactEntity)
            }
        }
        emit(contacts.toList())
    }
}