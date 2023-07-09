package ziyari.mahan.ashena.data.repository

import contacts.core.Contacts
import contacts.core.entities.NewName
import contacts.core.entities.NewPhone
import contacts.core.entities.NewRawContact
import contacts.core.util.addEmail
import contacts.core.util.addPhone
import contacts.core.util.setName
import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import javax.inject.Inject

class AddContactRepository @Inject constructor(private val dao: ContactDao) {

    @Inject
    lateinit var contactApi: Contacts
    suspend fun insertContact(contactEntity: ContactEntity) = dao.insertContact(contactEntity)

    suspend fun insertToDevice(contactEntity: ContactEntity): Boolean {
        val insertResult = contactApi
            .insert()
            .rawContact{
                setName {
                    givenName = contactEntity.firstName
                    familyName = contactEntity.lastName
                }
                addPhone {
                    number = contactEntity.number
                }
                addEmail {
                    address = contactEntity.email
                }
            }.commit()

        return insertResult.isSuccessful
    }
}