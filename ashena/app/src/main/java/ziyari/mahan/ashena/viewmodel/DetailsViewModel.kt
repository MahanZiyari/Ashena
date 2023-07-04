package ziyari.mahan.ashena.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.data.repository.DetailsRepository
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: DetailsRepository): ViewModel() {
    var contact = MutableLiveData<ContactEntity>()

    fun getContactFromDatabaseWith(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.getContactWith(id).collect {
            contact.postValue(it)
        }
    }

    fun getContactFromPhone(id: Int) = viewModelScope.launch {
        repository.getSpecificContactWith(id).collect {
            contact.postValue(it)
        }
    }

    fun updateDatabaseContact(contactEntity: ContactEntity) = viewModelScope.launch {
        repository.updateContact(contactEntity)
    }

    fun updatePhoneContact(contactId: Int) = viewModelScope.launch(Dispatchers.IO) {

    }

    fun removeContact(contactEntity: ContactEntity) = viewModelScope.launch {
        repository.removeContact(contactEntity)
    }

    fun removeContactFromDevice(id: Long): Boolean {
        var deleteResult = false
        viewModelScope.launch {
            deleteResult = repository.removeContactFromDevice(id)
        }
        return deleteResult
    }

}