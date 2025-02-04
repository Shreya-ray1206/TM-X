package org.kibbcom.tm_x

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.kibbcom.tm_x.ble.ScanningViewModel

// Actual Android implementation of the ViewModelFactory
actual class ScanningViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return ScanningViewModel() as T
    }
}