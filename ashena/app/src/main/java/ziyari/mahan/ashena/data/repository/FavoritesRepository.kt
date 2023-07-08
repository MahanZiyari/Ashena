package ziyari.mahan.ashena.data.repository

import contacts.core.Contacts
import contacts.core.entities.Options
import contacts.core.equalTo
import contacts.permissions.queryWithPermission
import kotlinx.coroutines.flow.flow
import ziyari.mahan.ashena.data.database.ContactDao
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.utils.PermissionsManager
import ziyari.mahan.ashena.utils.toEntity
import javax.inject.Inject

class FavoritesRepository @Inject constructor(private val dao: ContactDao) {

    @Inject
    lateinit var contactApi: Contacts
    @Inject
    lateinit var permissionsManager: PermissionsManager
    fun getFavoritesContactFromDatabase() = dao.getFavoritesContacts()

    fun getStarredContactsFromDevice() = flow {
        val result = mutableSetOf<ContactEntity>()
        if (permissionsManager.hasReadContactsPermissionGranted()) {
            contactApi
                .query()
                .where { Contact.Options.Starred equalTo  true}
                .find()
                .forEach {
                    result.add(it.toEntity())
                }
        }
        emit(result.toList())
    }
}