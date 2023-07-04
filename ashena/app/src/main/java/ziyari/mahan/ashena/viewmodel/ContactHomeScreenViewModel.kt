package ziyari.mahan.ashena.viewmodel

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import timber.log.Timber
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.data.repository.ContactHomeScreenRepository
import ziyari.mahan.ashena.utils.DEBUG_TAG
import ziyari.mahan.ashena.utils.DataStatus
import java.util.Timer
import javax.inject.Inject

@HiltViewModel
class ContactHomeScreenViewModel @Inject constructor(private val contactHomeScreenRepository: ContactHomeScreenRepository) :
    ViewModel() {
    var allContacts = MutableLiveData<DataStatus<List<ContactEntity>>>()

    fun getAllContactsFromDb() = viewModelScope.launch {
        contactHomeScreenRepository.getAllContactsFromDatabase().collect {
            allContacts.postValue(DataStatus.success(it, it.isEmpty()))
        }
    }

    @OptIn(FlowPreview::class)
    fun getAllContacts() = viewModelScope.launch {
        val allContactsFromBothSources = mutableSetOf<ContactEntity>()

        contactHomeScreenRepository.getAllContactsFromDatabase().flatMapConcat { phoneContacts ->
            allContactsFromBothSources.addAll(phoneContacts)
            contactHomeScreenRepository.getDeviceContactsUsingReborn()
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

    fun getDeviceContacts() = viewModelScope.launch(Dispatchers.IO) {
        contactHomeScreenRepository.getContacts().collect { contacts ->
            allContacts.postValue(DataStatus.success(contacts, contacts.isEmpty()))
        }
    }
}