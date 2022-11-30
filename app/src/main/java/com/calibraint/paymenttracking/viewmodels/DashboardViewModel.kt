package com.calibraint.paymenttracking.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calibraint.paymenttracking.data.HouseDetails
import com.calibraint.paymenttracking.repository.DatabaseRepository
import com.calibraint.paymenttracking.room.HouseMember
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DatabaseRepository
) : ViewModel() {

    var userData = MutableLiveData<HouseMember>()
    var toUpdateUser = MutableLiveData<Boolean>()
    var flatData = MutableLiveData<List<HouseMember>>()
    var haveAllPaid = MutableLiveData<Boolean>()
    val allData = MutableLiveData<ArrayList<HouseDetails>>()
    var isDataPresent = MutableLiveData<Boolean>()

    val flatPayment = MutableLiveData<String>()
    val flatPaymentMode = MutableLiveData<String>()

    fun getData(onCompletion: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            flatData.postValue(repository.getAllInfo())
            delay(50)
            isDataPresent.postValue(flatData.value?.isNotEmpty())
            val havePaid = flatData.value?.filter { !it.isPaid }.isNullOrEmpty()
            haveAllPaid.postValue(havePaid)
            onCompletion()
        }
    }

    fun addUser(
        flatNumber: String,
        primaryNumber: String,
        flatInfo: HouseMember,
        onFlatPresent: () -> Unit,
        onNumberPresent: () -> Unit,
        onSuccessfulAddition: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when {
                flatData.value!!.any { e -> e.flatNumber == flatNumber } -> onFlatPresent()
                flatData.value!!.any { e -> e.primaryNumber == primaryNumber } -> onNumberPresent()
                else -> {
                    repository.addUser(flatInfo)
                    onSuccessfulAddition()
                }
            }
        }
    }

    fun deleteUser(flatInfo: HouseMember, onCompletion: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(flatInfo)
            onCompletion()
        }
    }

    fun onPaymentMade(
        flatNo: String,
        isPaid: Boolean,
        paymentMode: String,
        onCompletion: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePayment(flatNo, isPaid, paymentMode)
            onCompletion()
        }
    }

    fun onClearAllTransactions(onCompletion: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllTransactions()
            onCompletion()
        }
    }

     fun clearAll(onCompletion: () -> Unit,onFailure: () -> Unit) {
         viewModelScope.launch(Dispatchers.IO) {
             if (!flatData.value.isNullOrEmpty()){
                 repository.deleteAllUsers()
                 onCompletion()
             } else onFailure()
         }
    }
}