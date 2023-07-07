package ziyari.mahan.ashena.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import ziyari.mahan.ashena.data.models.ContactEntity
import ziyari.mahan.ashena.data.repository.FavoritesRepository
import ziyari.mahan.ashena.utils.DataStatus
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {
    var favContacts = MutableLiveData<DataStatus<List<ContactEntity>>>()

    fun getAllFavoritesContacts() = viewModelScope.launch {
        val allContactsFromBothSources = mutableSetOf<ContactEntity>()

        repository.getFavoritesContactFromDatabase().flatMapConcat { db ->
            allContactsFromBothSources.addAll(db)
            repository.getStarredContactsFromDevice()
        }.collect { device ->
            allContactsFromBothSources.addAll(device)
            val finalContactsList = allContactsFromBothSources
                .toList()
                .sortedBy { it.firstName }

            favContacts.postValue(
                DataStatus.success(
                    finalContactsList,
                    finalContactsList.isEmpty()
                )
            )
        }
    }
}