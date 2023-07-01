package ziyari.mahan.ashena.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ziyari.mahan.ashena.utils.showDebugLog
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(): ViewModel() {
    var snackbarMessage = MutableLiveData<String>()

    fun showSnackbar(message: String) {
        showDebugLog("Snack Bar message: $message")
        snackbarMessage.postValue(message)
    }
}