package ziyari.mahan.ashena.data.repository

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.DEBUG_TAG
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val dao: ContactDao,
    @ApplicationContext private val context: Context
) {
    fun getContactWith(id: Int) = dao.getSingleContact(id)
    suspend fun updateContact(contactEntity: ContactEntity) = dao.updateContact(contactEntity)
    suspend fun removeContact(contactEntity: ContactEntity) = dao.removeContact(contactEntity)

    fun getContactFromPhone(id: Int)  = flow<ContactEntity> {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(id.toString()),
            null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val isStarred = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.STARRED)) == 1
            val labels = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL))

            // emit the contact information using a flow
            val entity = ContactEntity(
                firstName = name.substringBefore(' '),
                lastName = name.substringAfter(' '),
                number = phoneNumber,
                favorites = isStarred,
                group = labels ?: ""
            )
            Log.i(DEBUG_TAG, "entity: $entity")
            emit(entity)
        }

        cursor?.close()
    }

    fun editPhoneContact(contactId: Int) {

    }
}