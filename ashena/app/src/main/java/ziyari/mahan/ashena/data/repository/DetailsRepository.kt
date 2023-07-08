package ziyari.mahan.ashena.data.repository

import android.content.Context
import androidx.core.net.toUri
import contacts.core.Contacts
import contacts.core.`in`
import contacts.core.util.PhotoData
import contacts.core.util.phones
import contacts.core.util.photoBytes
import contacts.core.util.photoInputStream
import contacts.core.util.removePhoto
import contacts.core.util.setName
import contacts.core.util.setPhoto
import contacts.permissions.deleteWithPermission
import contacts.permissions.updateWithPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.getBitmapFromUri
import ziyari.mahan.ashena.utils.showDebugLog
import ziyari.mahan.ashena.utils.toEntity
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

        showDebugLog("Repo: ${contact.photoInputStream(contactApi)}")
        val modelContact = contact.toEntity()

        emit(modelContact)
    }


    suspend fun modifyPhoneContact(contactEntity: ContactEntity): Boolean {

        showDebugLog("Repo 2: ${contactEntity.profilePicture.byteInputStream()}")


        val contactToModify = contactApi
            .query()
            .where { Contact.Id `in` listOf(contactEntity.id.toLong()) }
            .find().first()

        val updateResult = contactApi
            .updateWithPermission()
            .contacts(
                contactToModify.mutableCopy {
                    if (contactEntity.profilePicture.isNotEmpty()) {
                        setPhoto(PhotoData.from(getBitmapFromUri(context, contactEntity.profilePicture.toUri())!!))
                    } else {
                        removePhoto()
                    }
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
                }
            )
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