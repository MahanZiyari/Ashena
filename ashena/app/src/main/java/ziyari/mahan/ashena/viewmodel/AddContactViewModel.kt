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
    val groups = MutableLiveData<MutableList<String>>()

    fun loadGroups() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf(Group.FAMILY.name, Group.WORK.name, Group.COSTUMERS.name, Group.FRIENDS.name)
        groups.postValue(data)
    }

    fun addContactToDatabase(contactEntity: ContactEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertContact(contactEntity)
    }


}