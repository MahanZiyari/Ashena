package ziyari.mahan.ashena.data.repository

import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import javax.inject.Inject

class AddContactRepository @Inject constructor(private val dao: ContactDao) {
    suspend fun insertContact(contactEntity: ContactEntity) = dao.insertContact(contactEntity)

}