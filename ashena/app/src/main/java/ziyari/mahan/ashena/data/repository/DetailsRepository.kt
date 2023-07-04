package ziyari.mahan.ashena.data.repository

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import contacts.core.Contacts
import contacts.core.equalTo
import contacts.core.`in`
import contacts.core.util.groupMemberships
import contacts.core.util.lookupKeyIn
import contacts.core.util.options
import contacts.core.util.phones
import contacts.permissions.deleteWithPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.DEBUG_TAG
import ziyari.mahan.ashena.utils.showDebugLog
import java.net.URI
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val dao: ContactDao,
    @ApplicationContext private val context: Context
) {
    @Inject
    lateinit var contactApi: Contacts
    fun getContactWith(id: Int) = dao.getSingleContact(id)
    suspend fun updateContact(contactEntity: ContactEntity) = dao.updateContact(contactEntity)
    suspend fun removeContact(contactEntity: ContactEntity) = dao.removeContact(contactEntity)

    fun getSpecificContactWith(id: Int) = flow {
        val contact = contactApi
            .query()
            .where { Contact.Id `in` listOf(id.toLong()) }
            .find().first()

        val number = if (contact.phones().toList().isEmpty()) "0000" else contact.phones().toList().first().normalizedNumber
        val group = if (contact.groupMemberships().toList().isEmpty()) "UNGroup" else contact.groupMemberships().toList().first().toString()
        
        val modelContact = ContactEntity(
            id = contact.id.toInt(),
            firstName = contact.displayNamePrimary ?: "Unnamed",
            lastName = "",
            profilePicture = contact.photoThumbnailUri.toString(),
            number = number ?: "",
            lookupKey = contact.lookupKey,
            group = group ?: "",
            favorites = contact.options()?.starred ?: false,
            isFromPhone = true
        )

        showDebugLog("Recieved Contact from API: $modelContact")
        emit(modelContact)
        
    }



    suspend fun modifyPhoneContact(contactId: Int) {

    }

    suspend fun removeContactFromDevice(id: Long): Boolean {

        val deleteResult = Contacts(context)
            .deleteWithPermission()
            .contactsWithId(id)
            .commit()
        
        return deleteResult.isSuccessful
    }
}