package ziyari.mahan.ashena.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SharedViewModel @Inject constructor(): ViewModel() {
    var snackBarCounter = MutableLiveData<Int>()
    var snackBarMessage = ""

    fun showSnackbar() {
        val newValue = snackBarCounter.value?.plus(1) ?: Random(52).nextInt()
        snackBarCounter.postValue(newValue)
    }
}