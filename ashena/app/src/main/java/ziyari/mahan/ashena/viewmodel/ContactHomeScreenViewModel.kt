package ziyari.mahan.ashena.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.data.repository.ContactHomeScreenRepository
import ziyari.mahan.ashena.utils.DataStatus
import javax.inject.Inject

@HiltViewModel
class ContactHomeScreenViewModel @Inject constructor(private val contactHomeScreenRepository: ContactHomeScreenRepository) :
    ViewModel() {
    var allContacts = MutableLiveData<DataStatus<List<ContactEntity>>>()

    fun getAllContactsFromDb() = viewModelScope.launch {
        contactHomeScreenRepository.getAllContactsFromTable().collect {
            allContacts.postValue(DataStatus.success(it, it.isEmpty()))
        }
    }
}