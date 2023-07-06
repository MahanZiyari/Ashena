package ziyari.mahan.ashena.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.data.repository.ContactHomeScreenRepository
import ziyari.mahan.ashena.utils.DataStatus
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ContactHomeScreenViewModel @Inject constructor(private val contactHomeScreenRepository: ContactHomeScreenRepository) :
    ViewModel() {
    var allContacts = MutableLiveData<DataStatus<List<ContactEntity>>>()

    fun getAllContactsFromDb() = viewModelScope.launch {
        contactHomeScreenRepository.getDatabaseContacts().collect {
            allContacts.postValue(DataStatus.success(it, it.isEmpty()))
        }
    }

    fun getAllContacts() = viewModelScope.launch(Dispatchers.IO) {
        val allContactsFromBothSources = mutableSetOf<ContactEntity>()

        contactHomeScreenRepository.getDatabaseContacts().flatMapConcat { phoneContacts ->
            allContactsFromBothSources.addAll(phoneContacts)
            contactHomeScreenRepository.getDeviceContacts()
        }.collect { databaseContacts ->
            allContactsFromBothSources.addAll(databaseContacts)
            val finalContactsList = allContactsFromBothSources
                .toList()
                .sortedBy { it.firstName }
            allContacts.postValue(
                DataStatus.success(
                    finalContactsList,
                    finalContactsList.isEmpty()
                )
            )
        }
    }

    fun getAllContacts(sortOption: Unit) = viewModelScope.launch(Dispatchers.IO) {
        val allContactsFromBothSources = mutableSetOf<ContactEntity>()

        contactHomeScreenRepository.getDatabaseContacts().flatMapConcat { phoneContacts ->
            allContactsFromBothSources.addAll(phoneContacts)
            contactHomeScreenRepository.getDeviceContacts()
        }.collect { databaseContacts ->
            allContactsFromBothSources.addAll(databaseContacts)
            val finalContactsList = allContactsFromBothSources
                .toList()
                .sortedBy { it.firstName }
            allContacts.postValue(
                DataStatus.success(
                    finalContactsList,
                    finalContactsList.isEmpty()
                )
            )
        }
    }


    fun searchForContacts(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        val contactsFound = mutableSetOf<ContactEntity>()

        contactHomeScreenRepository.getDatabaseContactsPartiallyMatchingName(searchQuery)
            .flatMapConcat { dbResult ->
                contactsFound.addAll(dbResult)
                contactHomeScreenRepository.getContactsPartiallyMatchingDisplayName(searchQuery)
            }.collect { deviceResult ->
                contactsFound.addAll(deviceResult)
                val finalResult = contactsFound.toList().sortedBy { it.firstName }
                allContacts.postValue(
                    DataStatus.success(finalResult, finalResult.isEmpty())
                )
            }
    }

}