package ziyari.mahan.ashena.data.repository

import android.content.Context
import contacts.core.Contacts
import contacts.core.contains
import contacts.permissions.queryWithPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.showDebugLog
import ziyari.mahan.ashena.utils.toEntity
import javax.inject.Inject

class ContactHomeScreenRepository @Inject constructor(
    private val contactDao: ContactDao,
    @ApplicationContext private val context: Context
) {

    @Inject
    lateinit var contactAPI: Contacts
    fun getDatabaseContacts() = contactDao.getAllContacts()
    fun getDatabaseContactsPartiallyMatchingName(searchQuery: String) =
        contactDao.seaarchContacts(searchQuery)

    fun getDeviceContacts() = flow {
        val modelContacts = mutableSetOf<ContactEntity>()
        val contacts = Contacts(context)
            .broadQuery()
            .find()

        contacts.forEach {
            modelContacts.add(it.toEntity())
        }

        emit(modelContacts.toList())
    }

    fun getContactsPartiallyMatchingDisplayName(searchQuery: String) = flow {
        val result = mutableSetOf<ContactEntity>()
        contactAPI
            .queryWithPermission()
            .where {
                Contact.DisplayNamePrimary contains searchQuery
            }
            .find()
            .forEach {
                showDebugLog("Reborn Found: $it\n")
                result.add(it.toEntity())
            }

        emit(result.toList())
    }
}