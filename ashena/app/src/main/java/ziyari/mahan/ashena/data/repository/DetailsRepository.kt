package ziyari.mahan.ashena.data.repository

import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val dao: ContactDao) {
    fun getContactWith(id: Int) = dao.getSingleContact(id)
    suspend fun updateContact(contactEntity: ContactEntity) = dao.updateContact(contactEntity)
    suspend fun removeContact(contactEntity: ContactEntity) = dao.removeContact(contactEntity)
}