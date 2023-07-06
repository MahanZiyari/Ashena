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
import contacts.core.util.groupMembershipList
import contacts.core.util.groupMemberships
import contacts.core.util.lookupKeyIn
import contacts.core.util.nameList
import contacts.core.util.names
import contacts.core.util.options
import contacts.core.util.phones
import contacts.core.util.setName
import contacts.permissions.deleteWithPermission
import contacts.permissions.updateWithPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.DEBUG_TAG
import ziyari.mahan.ashena.utils.showDebugLog
import ziyari.mahan.ashena.utils.toEntity
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

        val modelContact = contact.toEntity()

        emit(modelContact)
    }



    suspend fun modifyPhoneContact(contactEntity: ContactEntity): Boolean {
        val contactToModify = contactApi
            .query()
            .where { Contact.Id `in` listOf(contactEntity.id.toLong()) }
            .find().first()

        val updateResult = contactApi
            .updateWithPermission()
            .contacts(contactToModify.mutableCopy {
                setName {
                    givenName = contactEntity.firstName
                    familyName = contactEntity.lastName
                }
                phones().first().apply {
                    number = contactEntity.number
                }
                options?.apply {
                    starred = contactEntity.favorites
                }
            })
            .commit()

        return updateResult.isSuccessful
    }

    suspend fun removeContactFromDevice(id: Long): Boolean {

        val deleteResult = Contacts(context)
            .deleteWithPermission()
            .contactsWithId(id)
            .commit()
        
        return deleteResult.isSuccessful
    }
}