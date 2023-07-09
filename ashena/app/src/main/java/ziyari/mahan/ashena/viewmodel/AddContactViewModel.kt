package ziyari.mahan.ashena.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.data.models.Group
import ziyari.mahan.ashena.data.repository.AddContactRepository

@HiltViewModel
class AddContactViewModel @Inject constructor(private val repository: AddContactRepository) : ViewModel() {

    fun addContactToDatabase(contactEntity: ContactEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertContact(contactEntity)
    }


    fun insertContactToDevice(contactEntity: ContactEntity): Boolean {
        var insertResult = false
        viewModelScope.launch(Dispatchers.IO) {
            insertResult = repository.insertToDevice(contactEntity)
        }
        return insertResult
    }
}