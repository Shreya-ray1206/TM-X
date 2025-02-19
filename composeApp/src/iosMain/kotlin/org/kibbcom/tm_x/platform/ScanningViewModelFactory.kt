package org.kibbcom.tm_x.platform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.kibbcom.tm_x.db.AppDatabase
import org.kibbcom.tm_x.viewmodel.ScanningViewModel
import kotlin.reflect.KClass

// Actual iOS implementation (example)
actual class ScanningViewModelFactory actual constructor(
    private val db: AppDatabase
)  : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return ScanningViewModel(db) as T
    }
}

