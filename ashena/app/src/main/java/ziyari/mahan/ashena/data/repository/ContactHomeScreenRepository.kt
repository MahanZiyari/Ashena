package ziyari.mahan.ashena.data.repository

import ziyari.mahan.ashena.data.database.ContactDao
import javax.inject.Inject

class ContactHomeScreenRepository @Inject constructor(private val contactDao: ContactDao) {

    fun getAllContactsFromTable() = contactDao.getAllContacts()
}