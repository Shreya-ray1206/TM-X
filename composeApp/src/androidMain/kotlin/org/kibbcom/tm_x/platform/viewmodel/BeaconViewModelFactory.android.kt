package org.kibbcom.tm_x.platform.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.kibbcom.tm_x.db.AppDatabase
import org.kibbcom.tm_x.viewmodel.BeaconViewModel

actual class BeaconViewModelFactory actual constructor(
    private val db: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return BeaconViewModel(db) as T
    }
}